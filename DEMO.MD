# Test Scenarios

todo: Wat ik demo is de effecten van de verschillende problemen die kunnen optreden 
bij een server. Wat gebeurd er als connectie traag is (setNetwerkDelay), wat gebeurd 
er als de read timeout hoog is (setNetworkDelay)? 

En wat is het effect van een hoge read timeout icm een grote payload 
(setLargeOfferResponse + setNetworkDelay). 

Of wat is het verschil in een traag netwerk en een trage service (setServiceDelay).


Vervolgens kan je dus met de geïmplementeerde resilience patterns in de service 
laten zien hoe zij omgaan met de vertragingen. Voor elk pattern is een implementatie 
en kan je even een moment pakken om die te laten zien.
Je kan de code doorlopen / debuggen en ook de network inspector van chrome gebruiken 
en de output logging om beter te laten zien wat er gebeurd.




## Breakerbox

Success          : successful calls
Short-Circuited  : nr of times the circuit breaker was called whilst it was closed
Bad Request      : service incorrectly called
Timeout          : called service times out
Rejected         : bulkhead rejections
Failure          : failures
Error            :

## Timeouts

### Out of the box configuration
No timeouts. Fast responding service and network

- run offers.rest
- run offers-lowload-performance.sh (50 rps)

- refresh : http://localhost:8080/offers

expected behavior: no problem

### Introduce network/service delay
- trigger network and service delay on wiremock server (500 / 2000)

- run offers.rest
- run offers-lowload-performance.sh (50 rps)

- refresh : http://localhost:8080/offers

expected behavior: 
   response times increase some will be really slow / service unavailable
         http://localhost:8081/metrics?pretty=true       

### Configure connection/read-timeouts (too low)
- update yaml file (set timeouts lower then delay : 25 / 75)
- restart application
- run offers.rest

expected behavior: each request times out

### Re-configure connection/read-timeouts
- update yaml file (set timeouts higher then delay - 550/2550)
- restart application
- run offers.rest
- run offers-lowload-performance.sh (50 rps)

expected behavior: 
    response times increase a bit
    some requests time out... some don't
    website remains available 
          http://localhost:8080/offers
 
    monitor trace for rejections / timeouts
 
## Circuit Breaker

### Increase load
- trigger network and service delay on wiremock server (10 / 100)
- update yaml file (set timeouts higher then delay - 15/150)
- restart application

- run offers.rest
- run offers-lowload-performance.sh

expected behavior: 
    response times increase a bit
    some requests time out... most don't
          monitor console -> sometimes a "Read timed out"
    website remains available
          http://localhost:8080/offers
          
- run offers-performance.sh (200 rps)          
          
expected behavior: 
    response times increase a bit more
    more time outs
          monitor console -> sometimes a "Read timed out"
    website sometimes unavailable
          http://localhost:8080/offers       
             
- circuit breaker : 
     threadpool semaphore strategy
     threshold 20
     exec timout 1000
     nr semaphores 10
     fall back max 10     
     
     verify : http://localhost:8080/tenacity/configuration/GET_SPECIAL_OFFERS     
 
- run offers-circuitbreaker-performance.sh (200 rps)          

expected behavior: 
    response times improve
    some time outs
    website remains available
          http://localhost:8080/offers
    monitor breakerbox dashboard
        

- circuit breaker : 
     threshold 40
     
expected behavior: not a lot of difference    
   
- circuit breaker :     
     semaphore : 50/50

expected behavior: 
     almost no short circuits and rejects, some failures (service time outs)
     observe difference:
         http://localhost:8080/offers
             slow, often unavailable
         http://localhost:8080/offers/circuitbreaker
             faster, mostly available
              
        
        
## Bulkhead Breaker        
        
- run offers-semaphore-performance.sh       
 
observe difference:
          http://localhost:8080/offers
              slow, often unavailable
          http://localhost:8080/offers/semaphore
              faster, available, sometimes fallback result       
        

- circuit breaker using thread handover (bulkhead for free): 
     threadpool isolation strategy
     threshold 50
     exec timout 1000
     threadpool size 50
     
     verify : http://localhost:8080/tenacity/configuration/GET_SPECIAL_OFFERS
          
- run offers-circuitbreaker-performance.sh (200 rps)          

observe difference:
      http://localhost:8080/offers
          slowish but available mostly
      http://localhost:8080/offers/circuitbreaker
          a bit faster, mostly available     
      monitor breakerbox dashboard
         

## Thread hangover

- run offers-threadhandover-performance.sh       
 
observe difference:
          http://localhost:8080/offers
              slow, often unavailable
          http://localhost:8080/offers/threadhandover
              faster, available, sometimes fallback result      
          


- run offers-hystrix-performance.sh 

observe:
          http://localhost:8080/offers/threadhandover
              faster, available, sometimes fallback result 

          monitor breakerbox dashboard


## Async 

- run offers-async-performance.sh

observe:
          http://localhost:8080/offers/async
              - a lot of failures / timeouts
              
          monitor breakerbox dashboard
              - short circuits + rejects
 
          -> the appplication is to fast for the underlying service
             remember we put a short delay on the service, lets remove it
             
- if it has stopped : run offers-async-performance.sh
      
          monitor breakerbox dashboard
              - no more circuits
              - still timeouts, but mostly below the threshold so circuit never trips

- increase semaphore settings : 20 / 50

monitor breakerbox dashboard
              - some circuits
              - still timeouts.... heavier load on back-end service
              - 34% errors

- in yml file increase the read-timeout to 250ms

monitor breakerbox dashboard
              - very few circuits
              - still timeouts.... heavier load on back-end service
              - 30% errors

- in yml file increase the read-timeout to 500ms

monitor breakerbox dashboard
              - 25% errors

- increase nr accepted calls bulkhead -> 50

monitor breakerbox dashboard
              - some circuits
              - still timeouts.... heavier load on back-end service
              - 30% errors

## With random delays

- on wiremock server set random delay
- set network delay to 50

- in yml set con.timeout 50
- in yml set read timeout 300

- run offers-lowload-performance.sh (50 rps)
  expected behavior: 
     - service available in the beginning : http://localhost:8080/offers
     - trace : many timeouts
     - site sometimes not available (slows down because of load)

- run offers-circuitbreaker-lowload-performance.sh (50 rps)
  expected behavior: 
     - a lot of client timeouts
     - a lot of circuit breaker trips
     - site remains fast, fallback results
     - often circuit breakers are tripped -> high error percentage (dashboard)

- run offers-threadhandover-lowload-performance.sh (200 rps)
  expected behavior: 
     - ondanks veel hogere load -> site blijft toch bereikbaar
     
- configure semaphore strategy for hystrix     
- run offers-hystrix-lowload-performance.sh (50 rps)
  expected behavior:      
     - a lot of client timeouts
     - a lot of circuit breaker trips
     - site remains fast
     - often circuit breakers are tripped -> high error percentage (dashboard)
     
 - increase read timeout to 900    
 - run offers-hystrix-lowload-performance.sh (50 rps)
   expected behavior: 
     - less timeouts 
     - almost no circuit breaker trips
     - but... the longer timeout -> more threads busy -> service may not be available (http://localhost:8080/offers)
     
     
     
     