from io import StringIO
import sys

if len(sys.argv) > 1:
    new_version = sys.argv[1]
else:
    new_version = input("Input new version: ")


def update_gradle_proj():
    new_gradle_properties = StringIO()
    with open("gradle.properties", "r", encoding="utf-8") as f:
        for line in f.readlines():
            if line.startswith("version="):
                new_gradle_properties.write("version=" + new_version)
                new_gradle_properties.write("\n")
            else:
                new_gradle_properties.write(line)

    with open("gradle.properties", "w+", encoding="utf-8") as f:
        f.write(new_gradle_properties.getvalue())


def update_python_proj():
    new_setup_py = StringIO()
    with open("python-lib/setup.py", "r", encoding="utf-8") as f:
        for line in f.readlines():
            if line.strip().startswith("version="):
                space_cnt = 0
                while line.startswith(" "):
                    space_cnt += 1
                    line = line[1:]
                while space_cnt > 0:
                    space_cnt -= 1
                    new_setup_py.write(" ")
                new_setup_py.write('version="' + new_version + '",')
                new_setup_py.write("\n")
            else:
                new_setup_py.write(line)

    with open("python-lib/setup.py", "w+", encoding="utf-8") as f:
        f.write(new_setup_py.getvalue())


def update_node_proj():
    new_package_json = StringIO()
    with open("js-lib/package.json", "r", encoding="utf-8") as f:
        for line in f.readlines():
            if line.strip().startswith('"version"'):
                space_cnt = 0
                while line.startswith(" "):
                    space_cnt += 1
                    line = line[1:]
                while space_cnt > 0:
                    space_cnt -= 1
                    new_package_json.write(" ")
                new_package_json.write('"version": "' + new_version + '",')
                new_package_json.write("\n")
            else:
                new_package_json.write(line)

    with open("js-lib/package.json", "w+", encoding="utf-8") as f:
        f.write(new_package_json.getvalue())


update_gradle_proj()
update_python_proj()
update_node_proj()
