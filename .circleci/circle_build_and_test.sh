#!/bin/bash
set -e
./gradlew build
./gradlew dependencies >> .circleci/artifacts/dependency_report.txt
./gradlew test