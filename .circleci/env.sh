#!/usr/bin/env bash

export git=$(sh /etc/profile; which git)

cd "${0%/*}"

export TAB=$'\t'
export NEW=$'\n'

export CIRCLE_DIR=$PWD
export PROJECT_DIR=${CIRCLE_DIR}/..
export RM_SCRIPT_DIR=${CIRCLE_DIR}/release_management_scripts

cd ${PROJECT_DIR}

function debug() {
  >&2 echo "DEBUG:$1"
}