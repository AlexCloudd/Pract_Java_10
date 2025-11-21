@echo off
echo Starting Eureka Server...
cd /d "%~dp0eureka-server"
if not exist "target\eureka-server-0.0.1-SNAPSHOT.jar" (
    echo JAR файл не найден. Запускаю сборку...
    call mvn clean package -DskipTests
    if errorlevel 1 (
        echo ❌ Ошибка сборки!
        pause
        exit /b 1
    )
)
echo Запуск Eureka Server...
java -jar target/eureka-server-0.0.1-SNAPSHOT.jar
pause
