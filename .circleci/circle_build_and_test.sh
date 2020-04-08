#!/usr/bin/env bash
cd $( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )
source ./env.sh

sudo mkdir -p /axe-android
sudo chmod 777 /axe-android
sudo chown ${USER} /axe-android

mkdir -p /axe-android/h2

./gradlew :axe-android:dependencies >> ${ARTIFACTS_PATH}/dependency_report.txt
./gradlew build
./gradlew test
./gradlew check
./gradlew codeCoverage
./gradlew spotbugsMain
