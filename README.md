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

## Test Scenarios

### Out of the box configuration
- run offers.rest
- run offers-performance.sh
expected behavior: the higher the load... the slower it gets

### Introduce network delay
- trigger network delay on wiremock server (500)
- run offers.rest
- run offers-performance.sh
expected behavior: 
   response times increase some will be really slow
         http://localhost:8081/metrics?pretty=true 

### Configure connection/read-timeouts (too low)
- update yaml file (set timeouts lower then delay)
- restart application
- run offers.rest
expected behavior: each request times out

### Configure connection/read-timeouts
- update yaml file (set timeouts higher then delay - 750/750)
- restart application
- run offers.rest
- run offers-performance.sh
expected behavior: 
    response times increase a bit, 500 is possible (time-out)
          http://localhost:8080/offers
    

- run offers-circuitbreaker-performance.sh
expected behavior: 
    response times increase a bit, all delays within tolerance
    some response return the fallback result
    tracing : breakpoints in hystrix commmand -> fallback is being triggered
    circuitbreaker is getting opened / closed : 
           http://localhost:8081/healthcheck?pretty=true
           http://localhost:8080/tenacity/circuitbreakers

### Introduce service delay

- remove network delay on wiremock server
- trigger service delay on wiremock server (700)
- run offers.rest
- run offers-performance.sh
expected behavior: 
   response times increase some will be really slow
         http://localhost:8081/metrics?pretty=true
   500 response my occur (time out triggered)      
         
- run offers-circuitbreaker-performance.sh
expected behavior: 
    response times increase a bit, all delays within tolerance
    some repsonse return the fallback result
    tracing : breakpoints in hystrix commmand -> fallback is being triggered
    circuitbreaker is getting opened / closed : 
           http://localhost:8081/healthcheck?pretty=true
           http://localhost:8080/tenacity/circuitbreakers         

### Limit number of request threads

- maxThreads: 50, maxQueuedRequests = 50 in yaml file
- restart application
- run offers-circuitbreaker-performance.sh
expected behavior: 
     some requests will be reject - monitor trace in debug mode


### semaphore / bulkhead


### threadhandover / bulkhead


### hystrix


### async






breakerbox : http://192.168.2.3:8080/

## Links

- https://github.com/Netflix/Hystrix/wiki/How-it-Works
- https://bencane.com/2012/07/16/tc-adding-simulated-network-latency-to-your-linux-server/
- https://quarkus.io/guides/microprofile-fault-tolerance
- git clone https://github.com/quarkusio/quarkus-quickstarts.git
- https://lordofthejars.github.io/quarkus-cheat-sheet/

- https://github.com/yammer/breakerbox


https://openliberty.io/blog/2019/01/24/async-rest-jaxrs-microprofile.html
     https://github.com/OpenLiberty/sample-async-rest
