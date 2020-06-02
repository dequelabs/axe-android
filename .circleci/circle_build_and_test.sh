#!/usr/bin/env bash
cd $( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )
source ./env.sh

sudo mkdir -p /axe-android
sudo chmod 777 /axe-android
sudo chown ${USER} /axe-android

mkdir -p /axe-android/h2

./gradlew :dependencies >> ${ARTIFACTS_PATH}/dependency_report.txt
./gradlew build
gradlew_output=$(./gradlew check); gradlew_return_code=$?
if (( gradlew_return_code != 0 )); then
  echo "Grade failed with exit status $gradlew_return_code" >&2
  echo "and output: $gradlew_output" >&2
fi
./gradlew codeCoverage
./gradlew test
