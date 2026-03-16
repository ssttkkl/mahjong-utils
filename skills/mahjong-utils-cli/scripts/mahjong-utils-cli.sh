#!/usr/bin/env bash
# Mahjong Utils CLI wrapper - auto-downloads binary if needed

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SKILL_DIR="$(dirname "$SCRIPT_DIR")"
REPO="ssttkkl/mahjong-utils"

# Detect OS and architecture
detect_platform() {
    local os=$(uname -s)
    local arch=$(uname -m)
    
    case "$os" in
        Darwin)
            [[ "$arch" == "arm64" ]] && echo "macosArm64" || echo "macosX64"
            ;;
        Linux)
            [[ "$arch" == "aarch64" ]] && echo "linuxArm64" || echo "linuxX64"
            ;;
        MINGW*|MSYS*|CYGWIN*)
            echo "mingwX64"
            ;;
        *)
            echo "unknown"
            ;;
    esac
}

# Check if Java 17+ is available
has_java17() {
    if command -v java &> /dev/null; then
        local version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
        [[ "$version" -ge 17 ]] && return 0
    fi
    return 1
}

# Find local build
find_local_build() {
    local repo_root="$SKILL_DIR/../.."
    local platform="$1"
    
    # Check for JVM JAR
    if has_java17; then
        local jar="$repo_root/mahjong-utils-cli/build/libs/mahjong-utils-cli-"*"-executable.jar"
        if ls $jar 2>/dev/null | head -1 | grep -q .; then
            echo "$(ls $jar 2>/dev/null | head -1)"
            return 0
        fi
    fi
    
    # Check for Native binary
    local native="$repo_root/mahjong-utils-cli/build/bin/$platform/releaseExecutable/mahjong-utils-cli.kexe"
    [[ -f "$native" ]] && echo "$native" && return 0
    
    return 1
}

# Download from GitHub release
download_binary() {
    local platform="$1"
    local use_jvm="$2"
    
    local version=$(curl -s "https://api.github.com/repos/$REPO/releases/latest" | grep '"tag_name"' | cut -d'"' -f4)
    [[ -z "$version" ]] && return 1
    
    echo "Downloading mahjong-utils-cli $version..." >&2
    
    if [[ "$use_jvm" == "true" ]]; then
        local url="https://github.com/$REPO/releases/download/$version/mahjong-utils-cli-jvm/mahjong-utils-cli-${version#v}-executable.jar"
        curl -fL -o "$SCRIPT_DIR/mahjong-utils-cli.jar" "$url" && echo "$SCRIPT_DIR/mahjong-utils-cli.jar"
    else
        local filename="mahjong-utils-cli.kexe"
        [[ "$platform" == "mingwX64" ]] && filename="mahjong-utils-cli.exe"
        local url="https://github.com/$REPO/releases/download/$version/mahjong-utils-cli-$platform/$filename"
        curl -fL -o "$SCRIPT_DIR/mahjong-utils-cli" "$url" && chmod +x "$SCRIPT_DIR/mahjong-utils-cli" && echo "$SCRIPT_DIR/mahjong-utils-cli"
    fi
}

# Main
main() {
    local platform=$(detect_platform)
    local binary=""
    
    # 1. Check cached binary
    if [[ -f "$SCRIPT_DIR/mahjong-utils-cli.jar" ]]; then
        binary="$SCRIPT_DIR/mahjong-utils-cli.jar"
    elif [[ -f "$SCRIPT_DIR/mahjong-utils-cli" ]]; then
        binary="$SCRIPT_DIR/mahjong-utils-cli"
    fi
    
    # 2. Check local build
    if [[ -z "$binary" ]]; then
        binary=$(find_local_build "$platform")
    fi
    
    # 3. Download from release
    if [[ -z "$binary" ]]; then
        binary=$(download_binary "$platform" "$(has_java17 && echo true || echo false)")
    fi
    
    # 4. Fail if still not found
    if [[ -z "$binary" ]]; then
        echo "Error: Cannot find or download mahjong-utils-cli binary" >&2
        exit 1
    fi
    
    # Run
    if [[ "$binary" == *.jar ]]; then
        exec java -jar "$binary" "$@"
    else
        exec "$binary" "$@"
    fi
}

main "$@"
