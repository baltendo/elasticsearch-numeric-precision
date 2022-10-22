# elasticsearch-numeric-precision

Check the precision of numeric data types in elasticsearch

## setup

docker network create elasticsearch-network

docker run -d --name elasticsearch --net elasticsearch-network -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.17.6

docker run -d --name kibana --net elasticsearch-network -p 5601:5601 kibana:7.17.6

## run

Use IDE to run App.java main

## result

```
Number of numbers: 9973
Number of digits after the dot: 15
---
javaBigDecimal:                        Result{sum=181191.612115683301572, avg=18.168215393129781}
elasticsearchFloat:                    Result{sum=181191.612095057964325, avg=18.168215391061661}
elasticsearchDouble:                   Result{sum=181191.612115683295997, avg=18.168215393129781}
elasticsearchScaledIntegralFractional: Result{sum=181191.612115683301376, avg=18.168215393129781}
elasticsearchScaledFloat:              Result{sum=181191.612115683325101, avg=18.168215393129781}
---
javaBigDecimal vs. elasticsearchFloat:                    Result{sum=0.000020625337247, avg=2.068120E-9}
javaBigDecimal vs. elasticsearchDouble:                   Result{sum=5.575E-12, avg=0E-15}
javaBigDecimal vs. elasticsearchScaledIntegralFractional: Result{sum=1.96E-13, avg=0E-15}
javaBigDecimal vs. elasticsearchScaledFloat:              Result{sum=2.3529E-11, avg=0E-15}
```
