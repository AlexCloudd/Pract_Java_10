@echo off
echo Starting Music Cloud Microservice...
java -jar target/pr3-0.0.1-SNAPSHOT.jar --spring.profiles.active=microservice --spring.config.name=application-microservice
pause
