#!/usr/bin/env bash
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )
source ./env.sh

./gradlew artifactoryPublish -PversionName=$1
./gradlew uploadArchives -PversionName=$1
