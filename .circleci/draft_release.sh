#!/usr/bin/env bash
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )
. ${DIR}/env.sh

set -e

# Capture variables and generate release notes.
version=$(${RM_SCRIPT_DIR}/detect_version.sh)
name="${version}"
tag=${version}

github_release_notes=$(.circleci/release_management_scripts/github_release_notes.sh)

json_data="{
  \"tag_name\":\"${tag}\",
  \"name\":\"${name}\",
  \"body\":\"${github_release_notes}\",
  \"draft\": true
}"

debug "${json_data}"

curl -u ${DQ_GITHUB_USERNAME}:${DQ_GITHUB_AUTH_TOKEN} \
  --header "Content-Type: application/json" \
  --data-binary "${json_data}" \
  https://api.github.com/repos/dequelabs/axe-android/releases

