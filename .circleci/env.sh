#!/usr/bin/env bash

#export git=$(sh /etc/profile; which git)
#
#cd "${0%/*}"
#
#export TAB=$'\t'
#export NEW=$'\n'

CIRCLE_CI_PATH=$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )
ROOT_PATH=${CIRCLE_CI_PATH}/../
ARTIFACTS_PATH=${CIRCLE_CI_PATH}/artifacts

cd ${ROOT_PATH}

#function debug() {
#  >&2 echo "DEBUG:$1"
#}