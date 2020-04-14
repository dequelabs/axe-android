#!/usr/bin/env bash
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )
source ./env.sh

echo "$DQ_AXE_GPG_KEY" | base64 -d > ./secret.gpg

./gradlew artifactoryPublish -PversionName=$1
./gradlew uploadArchives \
  -PversionName=$1 \
  -Psigning.secretKeyRingFile=./secret.gpg \
  -Psigning.keyId=${DQ_AXE_GPG_SINGING_KEY_ID} \
  -Psigning.password=${DQ_AXE_GPG_SIGNING_PASSWORD}
