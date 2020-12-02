# Design for failure: What if the Network Fails?

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

Start from Intellij

Create a run configuration:
- give it a proper name like ```demo```
- add a run configuration
- select ```eu.luminis.devcon.fallacies.UiServices``` as main class
- add ```server config.yaml``` as program arguments
- select project root folder as working directory
- save the configuration

Now you can run it from your IDE, and even debug it!

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
 And play with the shell scripts in the root folder.

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

Start from Intellij

Create a run configuration:
- give it a proper name like ```demo-breakerbox```
- add a run configuration
- select ```eu.luminis.devcon.fallacies.UiServices``` as main class
- add ```server breaker-box-config.yaml``` as program arguments
- select project root folder as working directory
- save the configuration

Now you can run it from your IDE, and even debug it!

## Call Rest Resources

The tests folder has rest example calls. You can run them straight from Intellij (after you install an http client plugin).

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

## Tool for calculating wiremock random settings

https://www.wolframalpha.com/input/?i=lognormaldistribution%28log%28220%29%2C+0.4%29

## Links

Resilience libraries

https://resilience4j.readme.io/docs
https://github.com/jhalterman/failsafe
https://quarkus.io/guides/microprofile-fault-tolerance
https://github.com/Netflix/Hystrix/wiki/How-it-Works

Interesting reads

https://12factor.net/
https://martinfowler.com/articles/microservices.html

https://dev.to/aws/why-are-services-slow-sometimes-mn3
https://aws.amazon.com/builders-library/using-load-shedding-to-avoid-overload/

https://docs.microsoft.com/en-us/azure/architecture/patterns/category/resiliency
https://dzone.com/articles/design-patterns-for-microservices-1
https://opensource.com/article/17/5/colorful-deployments
https://blog.codecentric.de/en/2019/06/resilience-design-patterns-retry-fallback-timeout-circuit-breaker/
https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-2rd-edition/content/en/part1/chapter13/server_asynchronous_response_processing.html
https://dzone.com/articles/understanding-the-cap-theorem

Example frameworks used

https://github.com/dropwizard/dropwizard
https://github.com/yammer/breakerbox
https://github.com/yammer/tenacity
http://www.batey.info/using-hystrix-with-dropwizard.html

Other

https://zipkin.io/
https://github.com/tomakehurst/saboteur
