#!/usr/bin/env bash
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )
. ${DIR}/env.sh

## Fetch all tags
## Check if currently on MASTER branch -- don't run rest of Script if not on MASTER
## Calculate version from last tag on Master
## Return valueX.valueY.z version
function releaseNotes() {
  commit_list="${1}"

  # Early exit if there are no commits
  if [[ -z ${commit_list} ]]; then
    echo "Error: Must run with new commits."
    exit 1
  fi

  NEW=$'\n'
  TAB=$'\t'

  fixes="Fixes:"

  debug "${commit_list}"

  while read -r line; do

    if [[ -n "${line}" ]]; then
      message=${line##*:}
      fixes="${fixes}${NEW}${TAB}${message}"
    fi

  done <<< "$(echo "${commit_list}" | grep -E ".*fix.*:.*")"

  if [[ "${fixes}" != *"Fixes:"$'\n'* ]]; then
    fixes=""
    features="Features:"
  else 
    features="${NEW}Features:"
  fi

  while read -r line; do

    if [[ -n "${line}" ]]; then
      message=${line##*:}
      features="${features}${NEW}${TAB}${message}"
    fi
    
  done <<< "$(echo "${commit_list}" | grep -E "(feat|feature).*:.*")"

  if [[ "${features}" != *"Features:"$'\n'* ]]; then
    features=""
  fi

  echo "${fixes}${features}"
}

# Call get_last_tag with module argument
last_tag=$(${git} describe --abbrev=0 --tags)

if [[ -z ${last_tag} ]]; then
    debug "No tag found for."
    exit 1
fi

## List of commits since last version
commit_list=$(${git} log ${last_tag}..HEAD --oneline --pretty=format:"%s" --reverse)

# Return last version if the are no new commits
if [[ -z ${commit_list} ]]; then
    last_version=$(echo ${last_tag} | cut -d'-' -f 2)
    debug ${last_version}
    exit 1
fi

echo "$(releaseNotes "${commit_list}")"
