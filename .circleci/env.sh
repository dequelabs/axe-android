#!/usr/bin/env bash

CIRCLE_CI_PATH=$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )
ROOT_PATH=${CIRCLE_CI_PATH}/../
ARTIFACTS_PATH=${CIRCLE_CI_PATH}/artifacts

cd ${ROOT_PATH}
