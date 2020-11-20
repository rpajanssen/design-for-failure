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

## Call Rest Resources

The tests folder has rest example calls. You can run them straight from Intellij (after you install an http client plugn).

Each file with the extension _.rest_ will execute one rest call.

All script files will execute a load test using curl on one of the resources. Start a load test and monitor what happens.

## Breakerbox

On running demo app:
- metrics                       : http://localhost:8081/
- list circuit breaker state    : http://localhost:8080/tenacity/circuitbreakers
- complete service config       : http://localhost:8080/tenacity/configuration/GET_SPECIAL_OFFERS

On the breaker box
- navigate to the dashboard     : http://192.168.2.3:8080
- list current configuration    : http://192.168.2.3:8080/archaius/production

## Links

- https://github.com/Netflix/Hystrix/wiki/How-it-Works
- https://bencane.com/2012/07/16/tc-adding-simulated-network-latency-to-your-linux-server/
- https://quarkus.io/guides/microprofile-fault-tolerance
- git clone https://github.com/quarkusio/quarkus-quickstarts.git
- https://lordofthejars.github.io/quarkus-cheat-sheet/

- https://github.com/yammer/breakerbox


https://openliberty.io/blog/2019/01/24/async-rest-jaxrs-microprofile.html
     https://github.com/OpenLiberty/sample-async-rest
