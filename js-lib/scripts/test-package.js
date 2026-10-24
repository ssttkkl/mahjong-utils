const assert = require("node:assert/strict");
const { execFileSync } = require("node:child_process");
const { mkdtempSync, mkdirSync, rmSync } = require("node:fs");
const { tmpdir } = require("node:os");
const { join } = require("node:path");

const packageDirectory = join(__dirname, "..");
const packageJson = require(join(packageDirectory, "package.json"));
const temporaryDirectory = mkdtempSync(join(tmpdir(), "mahjong-utils-package-test-"));
const npmEnvironment = {
  ...process.env,
  npm_config_cache: join(temporaryDirectory, "npm-cache"),
};

try {
  rmSync(join(packageDirectory, "dist"), { recursive: true, force: true });

  // npm pack runs prepack, so this also verifies that a clean checkout builds its artifacts.
  const packedPackages = JSON.parse(
    execFileSync(
      "npm",
      ["pack", "--json", "--pack-destination", temporaryDirectory],
      { cwd: packageDirectory, encoding: "utf8", env: npmEnvironment },
    ),
  );
  const packedPackage = packedPackages[0];
  const packedFiles = new Set(packedPackage.files.map(({ path }) => path));

  assert(packedFiles.has("dist/index.js"), "npm tarball must include dist/index.js");
  assert(packedFiles.has("dist/index.d.ts"), "npm tarball must include dist/index.d.ts");

  const consumerDirectory = join(temporaryDirectory, "consumer");
  mkdirSync(consumerDirectory);
  execFileSync(
    "npm",
    [
      "install",
      "--prefix",
      consumerDirectory,
      "--ignore-scripts",
      "--no-audit",
      "--no-fund",
      join(temporaryDirectory, packedPackage.filename),
    ],
    { stdio: "inherit", env: npmEnvironment },
  );
  execFileSync(
    process.execPath,
    [
      "-e",
      "const assert = require('node:assert/strict'); const api = require('mahjong-utils'); assert.equal(typeof api.shanten, 'function');",
    ],
    { cwd: consumerDirectory, stdio: "inherit" },
  );

  console.log(`Packed ${packageJson.name}@${packageJson.version} installs and imports successfully.`);
} finally {
  rmSync(temporaryDirectory, { recursive: true, force: true });
}
