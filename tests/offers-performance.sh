seq 1 100000 | xargs -n1 -P200 bash -c 'url="http://localhost:8080/offers"; curl -O -s $url'