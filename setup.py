from os import system
import subprocess
import sys
from distutils import log
from distutils.command.clean import clean as origin_clean
from distutils.errors import DistutilsExecError
from distutils.file_util import copy_file
from distutils.util import get_platform
from pathlib import Path

from setuptools import Command, setup
from setuptools.command.build_py import build_py as origin_build_py


def run_gradle_task(root, task):
    if sys.platform == 'win32':
        gradlew = "gradlew.bat"
        cmd = f"{gradlew} {task}"
    else:
        gradlew = "gradlew"
        system(f"chmod +x {gradlew}")
        cmd = f"./{gradlew} {task}"

    log.info(f"running {cmd} (in {root})")
    call_return = subprocess.call(cmd, shell=True, cwd=root)
    if call_return != 0:
        raise DistutilsExecError(f"gradlew returned an non-zero value {call_return}")


class build_py(origin_build_py):
    def run(self):
        self.run_command('build_kt')
        return super().run()


class build_kt(Command):
    user_options = [
        ('build-lib=', 'd', "directory to \"build\" (copy) to"),
        ('kt-libraries=', None, ''),
        ('shared-location=', None, ""),
    ]

    def initialize_options(self) -> None:
        self.build_lib = None
        self.shared_location = None
        self.kt_libraries = None

    def finalize_options(self) -> None:
        self.set_undefined_options('build_py',
                                   ('build_lib', 'build_lib'))

    def get_kt_build_dir(self, build_info):
        build_dir = Path(build_info.get("root"))
        subproject = build_info.get("subproject", None)
        if subproject is not None:
            build_dir = build_dir / subproject
        build_dir = build_dir / "build" / "bin" / "currentOs" / "releaseShared"
        return build_dir

    def build_sharedlib(self):
        out_dir = Path(self.build_lib) / self.shared_location
        out_dir.mkdir(exist_ok=True, parents=True)

        for (lib_name, build_info) in self.kt_libraries:
            log.info("building '%s' Kotlin/Native Shared Library", lib_name)

            root = Path(build_info.get("root")).absolute()

            task = ""
            subproject = build_info.get("subproject", None)
            if subproject is not None:
                task += f":{subproject}:"
            task += "linkReleaseSharedForCurrentOs"

            run_gradle_task(root, task)

            build_dir = self.get_kt_build_dir(build_info)

            for file in build_dir.iterdir():
                if sys.platform == 'win32' and file.name.endswith(".dll"):  # windows
                    copy_file(str(file), str(out_dir))
                elif sys.platform == 'darwin' and file.name.endswith(".dylib"):  # macOS
                    copy_file(str(file), str(out_dir))
                elif file.name.endswith(".so"):  # unix/linux
                    copy_file(str(file), str(out_dir))


    def run(self):
        self.build_sharedlib()


class clean(origin_clean):
    def run(self) -> None:
        super().run()
        self.run_command("clean_kt")


class clean_kt(Command):
    user_options = [
        ('kt-libraries=', None, ''),
    ]

    def initialize_options(self) -> None:
        self.kt_libraries = None

    def finalize_options(self) -> None:
        self.set_undefined_options('build_kt',
                                   ('kt_libraries', 'kt_libraries'))

    def run(self):
        for (lib_name, build_info) in self.kt_libraries:
            log.info("cleaning '%s' Kotlin/Native Shared Library", lib_name)

            root = Path(build_info.get("root")).absolute()
            run_gradle_task(root, "clean")


with open("README.md", "r", encoding="utf-8") as f:
    long_description = f.read()

setup(
    name="mahjong-utils",
    version="0.6.1",
    author="ssttkkl",
    author_email="huang.wen.long@hotmail.com",
    license="MIT",
    url="https://github.com/ssttkkl/mahjong-utils",
    description="日麻小工具",
    long_description=long_description,
    long_description_content_type="text/markdown",
    install_requires=[
        "pydantic>=1.9.0,<2.0.0",
        "cffi>=1.15.1",
        "stringcase>=1.2.0"
    ],
    packages=[
        "mahjong_utils",
        "mahjong_utils.bridge",
        "mahjong_utils.bridge.lib",
        "mahjong_utils.bridge.webapi_jar",
        "mahjong_utils.hora",
        "mahjong_utils.models",
        "mahjong_utils.point_by_han_hu",
        "mahjong_utils.shanten",
        "mahjong_utils.yaku"
    ],
    package_data={"": ["*_api.i"]},
    zip_safe=False,
    options={
        "build_kt": {
            "kt_libraries": [
                ("libmahjongutils",
                 {
                     "root": "kt",
                     "subproject": "mahjong-utils-entry"
                 })
            ],
            "shared_location": "mahjong_utils/bridge/lib"
        },
        "bdist_wheel": {
            "plat_name": get_platform()
        }
    },
    cmdclass={"build_kt": build_kt, "build_py": build_py, "clean": clean, "clean_kt": clean_kt}
)
