#!/usr/bin/env bash
export SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
export git=$(sh /etc/profile; which git)

TAB=$'\t'
NEW=$'\n'

function debug() {
  >&2 echo "DEBUG:$1"
}

function isolateCommits() {
  module_name=$1
  commits=$2

  echo "$(echo "${commits}" | grep -E "([\s,\(]+${module_name}[\s,\)]+.*:)|([\s,\(]+all[\s,\)]+.*:)")"
}
