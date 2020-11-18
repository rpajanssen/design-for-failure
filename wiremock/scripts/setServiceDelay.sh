#!/usr/bin/env bash
curl -X POST -d "{ \"fixedDelay\": $1}" http://localhost:8082/__admin/settings --header "Content-Type:application/json"