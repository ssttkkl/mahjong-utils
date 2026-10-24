"""Publish signed 0.8.0 files via Portal API; no staging deletion or retry."""
import base64
import hashlib
import json
import os
from pathlib import Path
import time
import urllib.error
import urllib.parse
import urllib.request
import uuid
import zipfile

files = {}
for path in Path('artifacts').rglob('*'):
    if not path.is_file() or '/0.8.0/' not in path.as_posix():
        continue
    if path.name.endswith(('.md5', '.sha1', '.sha256', '.sha512')):
        continue
    artifact = path.as_posix().split('/io/github/ssttkkl/', 1)[1].split('/')[0]
    origin = path.parts[1]
    owner = 'maven-ubuntu-latest' if artifact.endswith('-linuxx64') else 'maven-windows-latest' if artifact.endswith('-mingwx64') else 'maven-macos-latest'
    if origin != owner:
        continue
    relative = 'io/github/ssttkkl/'  + path.as_posix().split('/io/github/ssttkkl/', 1)[1]
    data = path.read_bytes()
    if relative in files and files[relative] != data:
        raise SystemExit('Conflicting platform artifact: ' + relative)
    files[relative] = data
assert files and any(p.endswith('.pom') for p in files)
for path in list(files):
    if path.endswith(('.pom', '.jar', '.klib', '.module')):
        assert path + '.asc' in files, 'Unsigned artifact: ' + path
# Any existing coordinate or uncertain response stops the entire upload.
for path in files:
    if not path.endswith('.pom'):
        continue
    try:
        with urllib.request.urlopen('https://repo.maven.apache.org/maven2/' + path, timeout=30):
            raise SystemExit('Coordinate already published: ' + path)
    except urllib.error.HTTPError as error:
        if error.code != 404:
            raise SystemExit('Coordinate check HTTP ' + str(error.code))
print('Existing coordinate checks: all absent; files:', len(files), flush=True)
with zipfile.ZipFile('central-0.8.0.zip', 'w', zipfile.ZIP_DEFLATED) as bundle:
    for path, data in sorted(files.items()):
        bundle.writestr(path, data)
        if not path.endswith(('.md5', '.sha1', '.sha256', '.sha512')):
            for algorithm in ('md5', 'sha1'):
                bundle.writestr(path + '.' + algorithm, hashlib.new(algorithm, data).hexdigest())
auth = 'Bearer ' + base64.b64encode((os.environ['CENTRAL_PORTAL_USERNAME'] + ':' + os.environ['CENTRAL_PORTAL_PASSWORD']).encode()).decode()
boundary = uuid.uuid4().hex
body = ('--' + boundary + '\r\nContent-Disposition: form-data; name="bundle"; filename="central-0.8.0.zip"\r\nContent-Type: application/octet-stream\r\n\r\n').encode() + Path('central-0.8.0.zip').read_bytes() + ('\r\n--' + boundary + '--\r\n').encode()
def call(path, data, content_type='application/json'):
    req = urllib.request.Request('https://central.sonatype.com/api/v1/publisher/' + path, data=data, headers={'Authorization': auth, 'Content-Type': content_type}, method='POST')
    try:
        with urllib.request.urlopen(req, timeout=120) as response:
            return response.read()
    except urllib.error.HTTPError as error:
        # Do not print arbitrary authenticated response contents.
        raise SystemExit('Portal HTTP ' + str(error.code))
deployment = call('upload?name=mahjong-utils-0.8.0&publishingType=AUTOMATIC', body, 'multipart/form-data; boundary=' + boundary).decode().strip().strip('"')
uuid.UUID(deployment)
print('Portal deployment:', deployment, flush=True)
for attempt in range(90):
    status = json.loads(call('status?id=' + urllib.parse.quote(deployment), b''))
    state = status.get('deploymentState')
    assert state in ('PENDING', 'VALIDATING', 'VALIDATED', 'PUBLISHING', 'PUBLISHED', 'FAILED')
    print('Portal state:', state, flush=True)
    if state == 'PUBLISHED':
        break
    if state == 'FAILED':
        print('Validation error artifact keys:', sorted(status.get('errors', {}).keys()), flush=True)
        raise SystemExit('Portal validation failed; retained for investigation')
    time.sleep(20)
else:
    raise SystemExit('Portal still pending; inspect deployment before retrying')
