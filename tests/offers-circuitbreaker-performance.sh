seq 1 100000 | xargs -n1 -P1000 bash -c 'url="http://localhost:8080/offers/circuitbreaker"; curl -O -s $url'