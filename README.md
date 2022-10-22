# elasticsearch-numeric-precision

## setup

docker network create elasticsearch-network

docker run -d --name elasticsearch --net elasticsearch-network -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.17.6

docker run -d --name kibana --net elasticsearch-network -p 5601:5601 kibana:7.17.6

## run

Use IDE to run App.java main

## result

2022-10-22 18:23:47.490 INFO  - Number of numbers: 9973
2022-10-22 18:23:47.490 INFO  - ---
2022-10-22 18:23:47.490 INFO  - javaBigDecimal:                        Result{sum=181191.612115683301572, avg=18.168215393129781}
2022-10-22 18:23:47.490 INFO  - elasticsearchFloat:                    Result{sum=181191.612095057964325, avg=18.168215391061661}
2022-10-22 18:23:47.490 INFO  - elasticsearchDouble:                   Result{sum=181191.612115683295997, avg=18.168215393129781}
2022-10-22 18:23:47.490 INFO  - elasticsearchScaledIntegralFractional: Result{sum=181191.612115683301376, avg=18.168215393129781}
2022-10-22 18:23:47.490 INFO  - elasticsearchScaledFloat:              Result{sum=181191.612115683325101, avg=18.168215393129781}
2022-10-22 18:23:47.490 INFO  - ---
2022-10-22 18:23:47.490 INFO  - javaBigDecimal vs. elasticsearchFloat:                    Result{sum=0.000020625337247, avg=2.068120E-9}
2022-10-22 18:23:47.490 INFO  - javaBigDecimal vs. elasticsearchDouble:                   Result{sum=5.575E-12, avg=0E-15}
2022-10-22 18:23:47.490 INFO  - javaBigDecimal vs. elasticsearchScaledIntegralFractional: Result{sum=1.96E-13, avg=0E-15}
2022-10-22 18:23:47.490 INFO  - javaBigDecimal vs. elasticsearchScaledFloat:              Result{sum=2.3529E-11, avg=0E-15}
