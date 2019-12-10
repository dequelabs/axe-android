#!/bin/bash
set -e
./gradlew dependencies >> .circleci/tmp/artifacts/dependency_report.txt
./gradlew artifactoryPublish