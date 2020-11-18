#!/usr/bin/env bash
java -jar wiremock-1.57-standalone.jar --port 8082 --no-request-journal &
disown