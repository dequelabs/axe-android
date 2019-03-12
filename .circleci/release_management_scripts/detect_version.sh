#!/usr/bin/env bash
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )
. ${DIR}/env.sh

## Fetch all tags
## Check if currently on MASTER branch -- don't run rest of Script if not on MASTER
## Calculate version from last tag on Master
## Return valueX.valueY.z version
function detectVersion() {
    last_tag=$1
    commit_list=$2

    ## MAJOR RELEASE: 1.0.0 update
    ## MINOR RELEASE: 0.1.0 update
    ## PATCH RELEASE: 0.0.1 update
    major_increment=0
    minor_increment=0
    patch_increment=0

    echo "${commits}" | grep -E -q "breaking:"

    if [[ -z '$(echo "${commits}" | grep -E -q "breaking:")' ]]; then
      major_increment=1
    elif [[ -z '$(echo "${commits}" | grep -E -q "(feat|feature):")' ]]; then
      minor_increment=1
    else
      patch_increment=1
    fi

    # Split tag into [*, *, *] format
    new_tag=$(echo ${last_tag} | cut -d'-' -f 2)
    new_tag=(${new_tag//./ })

    # Add all increments from specific repo and 'all' repo commits
    new_tag[0]=$((${new_tag[0]}+${major_increment}))
    new_tag[1]=$((${new_tag[1]}+${minor_increment}))
    new_tag[2]=$((${new_tag[2]}+${patch_increment}))

    # Re-include '.' characters
    new_tag=${new_tag[@]}; new_tag=${new_tag// /.};

    echo ${new_tag}
}

# Call get_last_tag with module argument
last_tag=$(${git} describe --abbrev=0 --tags)

if [[ -z ${last_tag} ]]; then
    echo "No tag found."
    exit 1
fi

## List of commits since last version
commit_list=$(${git} log ${last_tag}..HEAD --oneline --pretty=format:"%s" --reverse)

echo $(detectVersion ${last_tag} "${commit_list}")
