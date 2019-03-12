#!/usr/bin/env bash
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )
. ${DIR}/env.sh

release_notes=$(${SCRIPT_DIR}/release_notes.sh)

github_release_notes=${release_notes//$'\t'/- }
github_release_notes=${github_release_notes//$'\n'/"\n"}
github_release_notes=${github_release_notes//Fixes:/### Fixes}
github_release_notes=${github_release_notes//Features:/"\n### Features"}


debug "${github_release_notes}"

echo "${github_release_notes}"