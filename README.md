# The Fallacies of Distributed Computing: What if the Network Fails?

Demo code used to demonstrate the fallacies of distributing computing and resilience patterns how to fix them.

## HowTo Run

Run Wiremock VM

```
cd wiremock
vagrant up
```

Run Ui Services:
```
mvn clean install
java -jar target/devcon-fallacies-1.0-SNAPSHOT.jar server config.yaml
```

## HowTo Test

The following URL's are exposed by the Ui Services demonstrating resilience patterns:

* No resilience (timeouts only): http://localhost:8080/offers
* CircuitBreaker: http://localhost:8080/offers/circuitbreaker
* Bulkheads: http://localhost:8080/offers/semaphore
* ThreadHandover: http://localhost:8080/offers/threadhandover
* Hystrix: http://localhost:8080/offers/hystrix
* Async: http://localhost:8080/offers/async

To change wiremock use the rest-interface or go to the shell as follows:
 ```
 cd wiremock
 vagrant ssh
 ```
 And play with the shell scripts in the ``/vagrant`` folder.

## With BreakerBox

Run BreakerBox VM
```
cd breakerbox
vagrant up
```

Restart the UI Service with the BreakerBox configuration:

```
java -jar target/devcon-fallacies-1.0-SNAPSHOT.jar server breaker-box-config.yaml
```



