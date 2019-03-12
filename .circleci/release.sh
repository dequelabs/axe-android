#!/usr/bin/env bash
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )
. ${DIR}/env.sh

set -e

${RM_SCRIPT_DIR}/merge.sh

./gradlew artifactoryPublish