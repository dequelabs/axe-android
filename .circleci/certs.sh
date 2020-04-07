#!/usr/bin/env bash
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )
. ${DIR}/env.sh

CERT_FILE=${CIRCLE_CI_PATH}/axe-android-services-key.json
echo ${DQ_ANDROID_SERVICES_JSON_BASE64} | base64 --decode > ${CERT_FILE}

