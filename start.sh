#!/usr/bin/env bash

docker-compose down
./gradlew clean && \
./gradlew shadowJar

docker-compose up -d

docker exec -dit $(docker-compose ps -q spark) bash ./usr/local/spark/sbin/start-master.sh
docker exec -dit $(docker-compose ps -q spark) bash ./usr/local/spark/sbin/start-slave.sh $MASTER
docker exec -dit $(docker-compose ps -q kafka) bash ./script/kafka.sh
docker exec -dit $(docker-compose ps -q spark) bash ./script/spark.sh

while true
do
    docker exec -it $(docker-compose ps -q cassandra) cqlsh -e "SELECT * FROM api.event;"
    sleep 5;
done

#docker inspect -f '{{ .NetworkSettings.IPAddress }}' docker-compose ps -q spark