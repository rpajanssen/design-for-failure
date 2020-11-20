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
   
- maxThreads: 50, maxQueuedRequests = 50 in yaml file
- restart application   
- run offers-performance.sh
expected behavior: 
    response times increase a bit, 500 is possible (time-out)
          http://localhost:8080/offers    
    some requests will be reject - monitor trace in debug mode - as a result of request queue flooding

- run offers-circuitbreaker-performance.sh
expected behavior: 
    response times increase a bit, all delays within tolerance
    some response return the fallback result
    tracing : breakpoints in hystrix commmand -> fallback is being triggered
    circuitbreaker is getting opened / closed : 
           http://localhost:8081/healthcheck?pretty=true
           http://localhost:8080/tenacity/circuitbreakers

- runnning -> execute plain /offers -> it mail very well hang



-----

{
"threadpool": {
"threadPoolCoreSize": 20,
"keepAliveTimeMinutes": 1,
"maxQueueSize": -1,
"queueSizeRejectionThreshold": 5,
"metricsRollingStatisticalWindowInMilliseconds": 10000,
"metricsRollingStatisticalWindowBuckets": 10
},
"circuitBreaker": {
"requestVolumeThreshold": 20,
"sleepWindowInMillis": 5000,
"errorThresholdPercentage": 50,
"metricsRollingStatisticalWindowInMilliseconds": 10000,
"metricsRollingStatisticalWindowBuckets": 10
},
"semaphore": {
"maxConcurrentRequests": 10,
"fallbackMaxConcurrentRequests": 10
},
"executionIsolationThreadTimeoutInMillis": 1000,
"executionIsolationStrategy": "THREAD"
}

small responses, 20 req/s -> no problem
small responses, 50 req/s -> rejections
   -> threadpool maxed out 
   -> increase to 50
small responses, 50 req/s -> no problem

large responses, service delay 500, 50 req/s -> no problem
   -> threadpool often maxed out, on the limit 
   
large responses, service delay 900, 50 req/s -> 
   -> threadpool often maxed out, on the limit, some rejected
   -> timeouts but no short circuits, so number of timeouts below the threshold    

large responses, service delay 900, exec.timout 1500ms, 50 req/s -> no problem
   -> threadpool often maxed out, on the limit ()
   
request thread pool : 50, queue 25
   -> no problems with calling the special offers... but offers service unreponsive
      -> try in browser
   -> even if service delay brought back to 500
      -> slow to unresponsive in the browser      

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
    some response return the fallback result
    tracing : breakpoints in hystrix commmand -> fallback is being triggered
    circuitbreaker is getting opened / closed         

### Limit number of request threads

- maxThreads: 50, maxQueuedRequests = 50 in yaml file
- restart application
- run offers-circuitbreaker-performance.sh
expected behavior: 
     some requests will be reject - monitor trace in debug mode - as a result of request queue flooding
     -> if cascading -> website will become non responsive

- restore maxThreads: 1024, maxQueuedRequests = 1024 in yaml file

### semaphore / bulkhead


### threadhandover / bulkhead


### hystrix

- in breakerbox
  - timeout 1000
  - thread, max threads 20

- run offers-hystrix-performance.sh
expected behavior: 
     bulkhead rejections
     circuit opening
     some requests will be reject - monitor trace in debug mode

- in breakerbox
  - timeout 3000
  - thread, max threads 40
expected behavior: 
     less/no rejections
     circuit opening less
     
- maxThreads: 50, maxQueuedRequests = 50 in yaml file
- restart application     




     
-> expect higher non blocking throughput, less rquests in request queue and fail fast
   so website will seem more responsive      
     
     
### async


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
