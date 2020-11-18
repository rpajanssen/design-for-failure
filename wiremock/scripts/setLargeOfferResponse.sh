#!/usr/bin/env bash
curl -X POST -d @large-offer.json http://localhost:8082/__admin/mappings/new --header "Content-Type:application/json"