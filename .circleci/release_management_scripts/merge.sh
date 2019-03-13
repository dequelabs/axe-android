#!/usr/bin/env bash
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )
. ${DIR}/env.sh

# Merge develop and master
${git} checkout master
${git} fetch --tags
${git} reset --hard origin/master
${git} merge --ff-only develop
${git} push
