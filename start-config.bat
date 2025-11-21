@echo off
echo Starting Config Server...
java -jar target/pr3-0.0.1-SNAPSHOT.jar --spring.profiles.active=config --spring.config.name=application-config
pause
