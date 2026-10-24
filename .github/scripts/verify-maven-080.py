"""Read-only comparison of Central files with original signed release builds."""
from concurrent.futures import ThreadPoolExecutor
import hashlib
from pathlib import Path
import urllib.error
import urllib.request

expected = {}
for p in Path('artifacts').rglob('*'):
    if not p.is_file() or '/0.8.0/' not in p.as_posix() or not p.name.endswith(('.pom', '.jar', '.klib', '.module', '.asc')):
        continue
    rel = 'io/github/ssttkkl/' + p.as_posix().split('/io/github/ssttkkl/', 1)[1]
    artifact = rel.split('/')[3]
    owner = 'maven-ubuntu-latest' if artifact.endswith('-linuxx64') else 'maven-windows-latest' if artifact.endswith('-mingwx64') else 'maven-macos-latest'
    if p.parts[1] == owner:
        expected[rel] = hashlib.sha256(p.read_bytes()).hexdigest()
assert expected

def check(item):
    path, digest = item
    try:
        with urllib.request.urlopen('https://repo.maven.apache.org/maven2/' + path, timeout=30) as r:
            actual = hashlib.sha256(r.read()).hexdigest()
        return path, 'MATCH' if actual == digest else 'DIGEST_MISMATCH'
    except urllib.error.HTTPError as e:
        return path, 'HTTP_' + str(e.code)
    except Exception:
        return path, 'TRANSPORT_ERROR'

with ThreadPoolExecutor(max_workers=6) as pool:
    results = list(pool.map(check, sorted(expected.items())))
failures = [(p,s) for p,s in results if s != 'MATCH']
print('Verified files:', len(results)-len(failures), '/', len(results))
print('Coordinates:', len({p.split('/')[3] for p in expected}))
for p,s in failures:
    print(s,p)
if failures:
    raise SystemExit('Central retrieval incomplete; no publication attempted')
print('All Central artifact SHA256 values match original signed release builds')
