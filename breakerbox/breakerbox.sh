#!/usr/bin/env bash
java -Darchaius.configurationSource.additionalUrls=file:/vagrant/config.properties -jar /vagrant/breakerbox-service-0.4.1.jar server /vagrant/breakerbox.yml &
disown