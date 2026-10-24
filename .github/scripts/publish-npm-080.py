"""OIDC recovery of exact release packages; skip already published versions."""
import json
from pathlib import Path
import subprocess
import time
import urllib.error
import urllib.request

root = Path('source').resolve()
assert subprocess.check_output(['git','-C',str(root),'rev-parse','HEAD'],text=True).strip() == '964f603761c91dd6b570c472675fcab12ca4897b'
packages = [('mahjong-utils-entry', None), ('mahjong-utils-entry-wasm', None), ('mahjong-utils', root/'js-lib')]
for name, directory in packages:
    if directory is None:
        candidates=[]
        for p in root.rglob('package.json'):
            if '/build/' not in p.as_posix(): continue
            try: data=json.loads(p.read_text())
            except Exception: continue
            if data.get('name')==name and data.get('version')=='0.8.0' and (p.parent/data.get('main','index.js')).is_file(): candidates.append(p.parent)
        assert candidates, 'No assembled package: '+name
        directory=sorted(candidates,key=lambda p:len(str(p)))[0]
    p=directory/'package.json';data=json.loads(p.read_text());assert data['name']==name and data['version']=='0.8.0'
    data['repository']={'type':'git','url':'git+https://github.com/ssttkkl/mahjong-utils.git'}
    p.write_text(json.dumps(data,indent=2)+'\n')
    packed=json.loads(subprocess.check_output(['npm','pack','--json'],cwd=directory,text=True))[0]
    tarball=directory/packed['filename']
    assert any(f['path']==data.get('main','index.js').removeprefix('./') for f in packed['files']), 'Entrypoint missing'
    try:
        with urllib.request.urlopen('https://registry.npmjs.org/'+name+'/0.8.0',timeout=30):
            print(name,'0.8.0 exists; skip without overwrite',flush=True);continue
    except urllib.error.HTTPError as e:
        if e.code!=404: raise
    subprocess.run(['npm','publish',str(tarball),'--provenance','--access','public'],cwd=directory,check=True)
    for attempt in range(60):
        try:
            with urllib.request.urlopen('https://registry.npmjs.org/'+name+'/0.8.0',timeout=30) as response:
                assert json.load(response)['version']=='0.8.0'
                break
        except urllib.error.HTTPError as error:
            if error.code != 404: raise
            time.sleep(10)
    else:
        raise SystemExit('Publish accepted but registry propagation pending; do not republish '+name)
    print(name,'0.8.0 registry verified',flush=True)
