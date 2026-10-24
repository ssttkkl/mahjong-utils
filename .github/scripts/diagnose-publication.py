"""Read-only publication diagnostics. Never emit credentials or raw responses."""
import base64
import json
import os
import urllib.error
import urllib.parse
import urllib.request


def request(url, credential):
    req = urllib.request.Request(url, data=b'' if '/publisher/status?' in url else None, headers={'Authorization': credential, 'Accept': 'application/json'})
    try:
        with urllib.request.urlopen(req, timeout=30) as response:
            return response.status, json.load(response)
    except urllib.error.HTTPError as error:
        return error.code, None
    except Exception:
        return 'transport_or_json_error', None


def npm():
    token = os.environ.get('NPM_TOKEN', '')
    if not token:
        print('npm: secret missing'); return
    auth = 'Bearer ' + token
    status, data = request('https://registry.npmjs.org/-/whoami', auth)
    print('npm identity HTTP:', status)
    username = data.get('username') if isinstance(data, dict) else None
    if not isinstance(username, str):
        return
    # Identity is reported only as a match, never as arbitrary response text.
    print('npm identity matches package maintainer ssttkkl:', username == 'ssttkkl')
    status, data = request('https://registry.npmjs.org/-/user/' + urllib.parse.quote(username, safe='') + '/package', auth)
    print('npm package access HTTP:', status)
    if isinstance(data, dict):
        for package in ('mahjong-utils', 'mahjong-utils-entry', 'mahjong-utils-entry-wasm'):
            access = data.get(package)
            print('npm access', package, access if access in ('read-write', 'read-only') else 'not_reported')


def maven():
    username = os.environ.get('CENTRAL_PORTAL_USERNAME', '')
    password = os.environ.get('CENTRAL_PORTAL_PASSWORD', '')
    if not username or not password:
        print('Maven: secret missing'); return
    auth = 'Bearer ' + base64.b64encode((username + ':' + password).encode()).decode()
    deployment = os.environ.get('CENTRAL_DEPLOYMENT_ID', '')
    if deployment:
        import uuid
        uuid.UUID(deployment)
        status, data = request('https://central.sonatype.com/api/v1/publisher/status?id=' + deployment, auth)
        print('Central validation HTTP:', status)
        if isinstance(data, dict):
            print('Central state:', data.get('deploymentState') if data.get('deploymentState') in ('FAILED','PUBLISHED','VALIDATING','PENDING','PUBLISHING','VALIDATED') else 'unknown')
            errors = data.get('errors', {})
            messages = sorted(set(str(message) for values in errors.values() for message in (values if isinstance(values,list) else [values]))) if isinstance(errors,dict) else []
            for message in messages:
                for secret in (username, password, auth, auth.removeprefix('Bearer ')):
                    message = message.replace(secret, '[redacted]')
                print('Central validation error:', message[:2000])
        return
    status, data = request('https://ossrh-staging-api.central.sonatype.com/manual/search/repositories?ip=any&profile_id=io.github.ssttkkl', auth)
    print('Maven staging search HTTP:', status)
    repositories = data.get('repositories') if isinstance(data, dict) else None
    if isinstance(repositories, list):
        print('Maven staging repositories:', len(repositories))
        target = 'io.github.ssttkkl--fc0f4cba-fe1d-44f0-b483-c9bd90b7bd79'
        matching = [repo for repo in repositories if isinstance(repo, dict) and repo.get('key') == target]
        print('Maven failed-release staging present:', bool(matching))
        for repo in matching:
            state = repo.get('state')
            print('Maven failed-release staging state:', state if state in ('open', 'closed', 'released') else 'unknown')
            print('Maven Portal deployment attached:', bool(repo.get('portal_deployment_id')))


if __name__ == '__main__':
    if not os.environ.get('CENTRAL_DEPLOYMENT_ID'):
        npm()
    maven()
