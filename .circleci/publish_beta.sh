#!/usr/bin/env bash
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )
. ${DIR}/env.sh

./gradlew artifactoryPublish

${CIRCLE_DIR}/draft_release.sh
