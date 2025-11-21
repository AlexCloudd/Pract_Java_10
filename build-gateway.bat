@echo off
echo Building API Gateway...
cd /d "%~dp0api-gateway"
mvn clean package -DskipTests
if errorlevel 1 (
    echo ❌ Ошибка сборки API Gateway!
    pause
    exit /b 1
)
echo ✅ API Gateway собран успешно!
cd ..
pause






