## Test Scenarios

### Out of the box configuration
No timeouts. Fast responding service and network

- run offers.rest
- run offers-lowload-performance.sh

- refresh : http://localhost:8080/offers

expected behavior: no problem

### Introduce network/service delay
- trigger network and service delay on wiremock server (500 / 2000)

- run offers.rest
- run offers-lowload-performance.sh

- refresh : http://localhost:8080/offers

expected behavior: 
   response times increase some will be really slow / service unavailable
         http://localhost:8081/metrics?pretty=true 

### Configure connection/read-timeouts (too low)
- update yaml file (set timeouts lower then delay : 25 / 75)
- restart application
- run offers.rest
expected behavior: each request times out

### Configure connection/read-timeouts
- update yaml file (set timeouts higher then delay - 50/2503)
- restart application
- run offers.rest
- run offers-lowload-performance.sh

expected behavior: 
    response times increase a bit, 2500+ is possible 
    some requests time out... some don't
    website remains available
          http://localhost:8080/offers
 
    some requests will be reject - monitor trace in debug mode - as a result of request queue flooding
 



### circuit breaker

- run offers-circuitbreaker-loadload-performance.sh

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

