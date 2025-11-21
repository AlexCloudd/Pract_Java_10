@echo off
echo ========================================
echo   Cinema Hub - Full Microservices Stack
echo ========================================
echo.

echo [1/7] Building project...
call build.bat
if errorlevel 1 (
    echo ❌ Build failed!
    pause
    exit /b 1
)
echo ✅ Build completed!

echo.
echo [2/7] Starting Eureka Server (Port 8761)...
start "Eureka Server" cmd /k "cd /d %~dp0 && start-eureka.bat"
echo ⏳ Waiting for Eureka Server to start...
timeout /t 15 /nobreak > nul

echo [3/7] Starting Config Server (Port 8888)...
start "Config Server" cmd /k "cd /d %~dp0 && start-config.bat"
echo ⏳ Waiting for Config Server to start...
timeout /t 10 /nobreak > nul

echo [4/7] Starting API Gateway (Port 8080)...
start "API Gateway" cmd /k "cd /d %~dp0 && start-gateway.bat"
echo ⏳ Waiting for API Gateway to start...
timeout /t 20 /nobreak > nul

echo [5/7] Starting Cinema Hub Service (Port 8081)...
start "Cinema Hub" cmd /k "cd /d %~dp0 && start-microservice.bat"
echo ⏳ Waiting for Cinema Hub to start...
timeout /t 10 /nobreak > nul

echo [6/7] Starting User Service (Port 8082)...
start "User Service" cmd /k "cd /d %~dp0user-service && mvn spring-boot:run"
echo ⏳ Waiting for User Service to start...
timeout /t 10 /nobreak > nul

echo [7/7] Starting Movie Service (Port 8083)...
start "Movie Service" cmd /k "cd /d %~dp0movie-service && mvn spring-boot:run"
echo ⏳ Waiting for Movie Service to start...
timeout /t 10 /nobreak > nul

echo [8/8] Starting Rating Service (Port 8084)...
start "Rating Service" cmd /k "cd /d %~dp0 && start-rating-service.bat"

echo.
echo ========================================
echo   🎉 ALL SERVICES STARTED SUCCESSFULLY!
echo ========================================
echo.
echo 📋 Available URLs:
echo   • Eureka Dashboard:    http://localhost:8761
echo   • Config Server:       http://localhost:8888/actuator/health
echo   • API Gateway:         http://localhost:8080
echo   • Cinema Hub:         http://localhost:8081
echo   • User Service:        http://localhost:8082
echo   • Movie Service:       http://localhost:8083
echo   • Rating Service:      http://localhost:8084
echo   • Swagger UI:          http://localhost:8080/swagger-ui.html
echo   • Actuator Health:     http://localhost:8080/actuator/health
echo.
echo 💡 Tips:
echo   • Check Eureka Dashboard to see all registered services
echo   • Use Ctrl+C in each console window to stop services
echo   • All services will auto-register with Eureka
echo.
echo Press any key to continue...
pause > nul
