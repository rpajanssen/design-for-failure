#!/usr/bin/env bash
java -Darchaius.configurationSource.additionalUrls=file:config.properties -jar breakerbox-service-0.4.1.jar server breakerbox.yml & disown