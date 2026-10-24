"""Exercise release downloads without network access or a local build."""
import os
from pathlib import Path
import shutil
import subprocess
import tempfile
import unittest

SCRIPT = Path(__file__).resolve().parents[1] / 'scripts' / 'mahjong-utils-cli.sh'

class WrapperTest(unittest.TestCase):
    def test_jvm_download_without_local_build(self):
        with tempfile.TemporaryDirectory() as root:
            root = Path(root)
            scripts = root / 'skills' / 'cli' / 'scripts'
            scripts.mkdir(parents=True)
            shutil.copy(SCRIPT, scripts)
            tools = root / 'tools'
            tools.mkdir()
            mocks = {
                'java': '#!/bin/bash\nif [[ "$1" == "-version" ]]; then echo \'openjdk version "17.0.1"\' >&2; else echo "RUN:$*"; fi\n',
                'curl': '#!/bin/bash\nif [[ "$1" == "-s" ]]; then echo \'{"tag_name": "v0.8.0"}\'; else [[ "$4" == "https://github.com/ssttkkl/mahjong-utils/releases/download/v0.8.0/mahjong-utils-cli-0.8.0-executable.jar" ]] || exit 7; touch "$3"; fi\n',
            }
            for name, body in mocks.items():
                path = tools / name
                path.write_text(body)
                path.chmod(0o755)
            result = subprocess.run(['bash', str(scripts / SCRIPT.name), 'point', '3', '40'], env={**os.environ, 'PATH': f'{tools}:/usr/bin:/bin'}, capture_output=True, text=True)
            self.assertEqual(result.returncode, 0, result.stderr)
            self.assertIn('RUN:-jar', result.stdout)
            self.assertIn('point 3 40', result.stdout)

if __name__ == '__main__':
    unittest.main()
