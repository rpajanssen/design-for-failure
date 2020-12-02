#!/usr/bin/env bash
curl -X POST -d @random-latency.json http://localhost:8082/__admin/settings --header "Content-Type:application/json"