# Releasing

Run `python3 update-version.py X.Y.Z` and commit the synchronized Gradle,
Python and JavaScript versions. Validate the final commit's Actions and external
checks before merging. Publishing a GitHub Release triggers the existing Maven,
npm, PyPI, CLI and WebApi release workflows; pushing a tag alone does not.
Never reuse an existing published version.

## Maven Central Portal migration

The Maven workflow uses Sonatype's supported OSSRH Staging API compatibility
service at `https://ossrh-staging-api.central.sonatype.com/service/local/`.
It explicitly creates a staging repository, passes its ID to every OS job,
and closes/releases that same repository after the matrix succeeds. This keeps
all platforms together rather than relying on IP-specific implicit repositories.
The staging profile is the namespace `io.github.ssttkkl`; confirm this namespace
is visible to the token owner before publishing.

Generate a Portal user token at https://central.sonatype.com/usertoken and enter
its two generated fields directly into GitHub Actions repository secrets:

- `CENTRAL_PORTAL_USERNAME`: token username (not the account login).
- `CENTRAL_PORTAL_PASSWORD`: token password (not the account password).

Repository secret settings:
https://github.com/ssttkkl/mahjong-utils/settings/secrets/actions

Do not paste tokens into chat, commits, logs or command arguments. The previous
OSSRH token and profile ID are no longer used by this workflow. Existing signing
secrets remain required: `SIGNING_KEYID`, `SIGNING_PASSWORD`, and
`SIGNING_SECRET_KEY_RING_BASE64`. Existing npm, PyPI trusted publishing and
container publishing configuration must also remain valid. A PR build cannot
prove those release-only credentials work.

Do not publish a GitHub Release until credentials and all channels are ready:
release workflows run independently, so one failure can leave a partial release.
After publication, inspect every release workflow and verify actual GitHub
assets, Maven Central coordinates, npm packages and PyPI version availability.

Official references:
- https://central.sonatype.org/pages/ossrh-eol
- https://central.sonatype.org/publish/publish-portal-ossrh-staging-api/
- https://central.sonatype.org/publish/generate-portal-token/
