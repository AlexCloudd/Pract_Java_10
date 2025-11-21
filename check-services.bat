@echo off
echo ========================================
echo   Проверка статуса микросервисов
echo ========================================
echo.

echo Проверка портов...
echo.

netstat -ano | findstr ":8080" >nul
if %errorlevel% == 0 (
    echo [✓] Порт 8080 (API Gateway) - ЗАНЯТ
) else (
    echo [✗] Порт 8080 (API Gateway) - СВОБОДЕН
)

netstat -ano | findstr ":8081" >nul
if %errorlevel% == 0 (
    echo [✓] Порт 8081 (Cinema Hub) - ЗАНЯТ
) else (
    echo [✗] Порт 8081 (Cinema Hub) - СВОБОДЕН
)

netstat -ano | findstr ":8082" >nul
if %errorlevel% == 0 (
    echo [✓] Порт 8082 (User Service) - ЗАНЯТ
) else (
    echo [✗] Порт 8082 (User Service) - СВОБОДЕН
)

netstat -ano | findstr ":8083" >nul
if %errorlevel% == 0 (
    echo [✓] Порт 8083 (Movie Service) - ЗАНЯТ
) else (
    echo [✗] Порт 8083 (Movie Service) - СВОБОДЕН
)

netstat -ano | findstr ":8084" >nul
if %errorlevel% == 0 (
    echo [✓] Порт 8084 (Rating Service) - ЗАНЯТ
) else (
    echo [✗] Порт 8084 (Rating Service) - СВОБОДЕН
)

netstat -ano | findstr ":8761" >nul
if %errorlevel% == 0 (
    echo [✓] Порт 8761 (Eureka Server) - ЗАНЯТ
) else (
    echo [✗] Порт 8761 (Eureka Server) - СВОБОДЕН
)

echo.
echo ========================================
echo   Проверка доступности сервисов
echo ========================================
echo.

echo Проверка Cinema Hub (8081)...
curl -s http://localhost:8081/actuator/health >nul 2>&1
if %errorlevel% == 0 (
    echo [✓] Cinema Hub доступен
) else (
    echo [✗] Cinema Hub недоступен
)

echo Проверка User Service (8082)...
curl -s http://localhost:8082/actuator/health >nul 2>&1
if %errorlevel% == 0 (
    echo [✓] User Service доступен
) else (
    echo [✗] User Service недоступен
)

echo Проверка Movie Service (8083)...
curl -s http://localhost:8083/actuator/health >nul 2>&1
if %errorlevel% == 0 (
    echo [✓] Movie Service доступен
) else (
    echo [✗] Movie Service недоступен
)

echo Проверка Rating Service (8084)...
curl -s http://localhost:8084/actuator/health >nul 2>&1
if %errorlevel% == 0 (
    echo [✓] Rating Service доступен
) else (
    echo [✗] Rating Service недоступен
)

echo Проверка API Gateway (8080)...
curl -s http://localhost:8080/actuator/health >nul 2>&1
if %errorlevel% == 0 (
    echo [✓] API Gateway доступен
) else (
    echo [✗] API Gateway недоступен
)

echo Проверка Eureka (8761)...
curl -s http://localhost:8761 >nul 2>&1
if %errorlevel% == 0 (
    echo [✓] Eureka Server доступен
) else (
    echo [✗] Eureka Server недоступен
)

echo.
echo ========================================
echo   Рекомендации
echo ========================================
echo.
echo Если сервисы не запущены, выполните:
echo   start-all.bat
echo.
echo Или запустите отдельно:
echo   start-eureka.bat
echo   start-microservice.bat
echo   start-gateway.bat
echo.
echo Для проверки API используйте:
echo   http://localhost:8081/actuator/health
echo   http://localhost:8082/api/users
echo   http://localhost:8083/api/movies
echo   http://localhost:8084/api/ratings
echo.
pause






