#!/bin/bash

if [ -z "${SONAR_TOKEN}" ]; then
    ./gradlew check
else
    ./gradlew check sonarqube
fi
