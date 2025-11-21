@echo off
echo Starting API Gateway...
cd /d "%~dp0api-gateway"
if not exist "target\api-gateway-0.0.1-SNAPSHOT.jar" (
    echo JAR файл не найден. Запускаю сборку...
    call mvn clean package -DskipTests
    if errorlevel 1 (
        echo ❌ Ошибка сборки!
        pause
        exit /b 1
    )
)
echo Запуск API Gateway...
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar
pause
