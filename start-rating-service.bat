@echo off
echo Starting Rating Service...
cd /d "%~dp0rating-service"
mvn spring-boot:run
