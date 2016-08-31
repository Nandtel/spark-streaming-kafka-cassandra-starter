#!/usr/bin/env bash
### Script which build and run applications
## Author: Sergey Petrovsky


#Shutdown all running Docker Compose containers
docker-compose down

#Clean and Build JARs of our application (kafka-producer, model-common, spark-consumer through shadowJar plugin, web-api through spring boot gradle plugin)
./gradlew clean && \
./gradlew shadowJar && \
./gradlew build -p ./web-api

#Start all containers with docker-compose in detached mode. The --rm flag makes docker-compose to delete corresponding spark container after run.
docker-compose up -d

printf "Application are deploying, until become 50: "

for i in {1..10}
do
   printf "$i->"
   sleep 1
done

#Start Spark master
docker exec -dit $(docker-compose ps -q spark) bash ./usr/local/spark/sbin/start-master.sh

for i in {11..20}
do
   printf "$i->"
   sleep 1
done

#Start Spark slave
docker exec -dit $(docker-compose ps -q spark) bash ./usr/local/spark/sbin/start-slave.sh spark://0.0.0.0:7077

#Run script which create Kafka topic
docker exec -dit $(docker-compose ps -q kafka) bash ./script/kafka.sh

for i in {21..30}
do
   printf "$i->"
   sleep 1
done

#Run script which submit Spark application
docker exec -dit $(docker-compose ps -q spark) bash ./script/spark.sh

for i in {31..50}
do
   printf "$i->"
   sleep 1
done
printf "Done!"

#Select amount of resend words
while true
do
    docker exec -it $(docker-compose ps -q cassandra) cqlsh -e "SELECT * FROM api.event;"
    sleep 5;
done

#docker inspect -f '{{ .NetworkSettings.IPAddress }}' docker-compose ps -q spark



