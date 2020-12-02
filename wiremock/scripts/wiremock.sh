#!/usr/bin/env bash
java -jar wiremock-jre8-standalone-2.27.2.jar --port 8082 --no-request-journal &
disown