#!/usr/bin/env bash
cd $( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )
source ./env.sh

sudo mkdir -p /sauron
sudo chmod 777 /sauron
sudo chown ${USER} /sauron

mkdir -p /sauron/h2

./gradlew :attest-db:dependencies >> ${ARTIFACTS_PATH}/dependency_report.txt
./gradlew build
./gradlew test
./gradlew check
./gradlew codeCoverage
./gradlew spotbugsMain
