"""Redirect only the immutable release's Maven destination to a local bundle."""
from pathlib import Path
import re
import subprocess

source = Path('source')
expected = '964f603761c91dd6b570c472675fcab12ca4897b'
assert subprocess.check_output(['git', '-C', str(source), 'rev-parse', 'HEAD'], text=True).strip() == expected
path = source / 'build-logic/convention/src/main/kotlin/mahjongutils/buildlogic/MavenPublishConventionPlugin.kt'
text = path.read_text()
start = text.index('                    val repositoryId =')
end = text.index('\n                }\n                maven {', start)
text = text[:start] + '                    setUrl(rootProject.layout.buildDirectory.dir("central-recovery").get().asFile.toURI())' + text[end:]
path.write_text(text)
