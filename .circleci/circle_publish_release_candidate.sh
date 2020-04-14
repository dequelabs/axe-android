#!/usr/bin/env bash
cd $( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )
source ./env.sh

./gradlew artifactoryPublish -PversionName=$1