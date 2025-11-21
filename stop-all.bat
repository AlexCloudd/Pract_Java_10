@echo off
echo ========================================
echo   Stopping Cinema Hub Microservices
echo ========================================
echo.

echo Stopping all Java processes related to Cinema Hub...

REM Kill processes by name
taskkill /f /im java.exe /fi "WINDOWTITLE eq Eureka Server*" 2>nul && echo ✅ Eureka Server stopped
taskkill /f /im java.exe /fi "WINDOWTITLE eq Config Server*" 2>nul && echo ✅ Config Server stopped
taskkill /f /im java.exe /fi "WINDOWTITLE eq API Gateway*" 2>nul && echo ✅ API Gateway stopped
taskkill /f /im java.exe /fi "WINDOWTITLE eq Cinema Hub*" 2>nul && echo ✅ Cinema Hub stopped
taskkill /f /im java.exe /fi "WINDOWTITLE eq User Service*" 2>nul && echo ✅ User Service stopped
taskkill /f /im java.exe /fi "WINDOWTITLE eq Movie Service*" 2>nul && echo ✅ Movie Service stopped
taskkill /f /im java.exe /fi "WINDOWTITLE eq Rating Service*" 2>nul && echo ✅ Rating Service stopped

REM Kill processes by port
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8761') do taskkill /f /pid %%a 2>nul && echo ✅ Process on port 8761 stopped
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8888') do taskkill /f /pid %%a 2>nul && echo ✅ Process on port 8888 stopped
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do taskkill /f /pid %%a 2>nul && echo ✅ Process on port 8080 stopped
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8081') do taskkill /f /pid %%a 2>nul && echo ✅ Process on port 8081 stopped
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8082') do taskkill /f /pid %%a 2>nul && echo ✅ Process on port 8082 stopped
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8083') do taskkill /f /pid %%a 2>nul && echo ✅ Process on port 8083 stopped
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8084') do taskkill /f /pid %%a 2>nul && echo ✅ Process on port 8084 stopped

REM Kill any remaining Maven processes
taskkill /f /im mvn.cmd 2>nul
taskkill /f /im mvn.exe 2>nul

echo.
echo ========================================
echo   🛑 ALL SERVICES STOPPED SUCCESSFULLY!
echo ========================================
echo All microservices have been stopped and cleaned up.
echo.
pause
