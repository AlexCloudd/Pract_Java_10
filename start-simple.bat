@echo off
echo ========================================
echo   Простой запуск основных сервисов
echo ========================================
echo.

echo [1/3] Проверка сборки проекта...
if not exist "target\pr3-0.0.1-SNAPSHOT.jar" (
    echo JAR файл не найден. Запускаю сборку...
    call build.bat
    if errorlevel 1 (
        echo ❌ Ошибка сборки!
        pause
        exit /b 1
    )
)

echo [2/3] Запуск Eureka Server (порт 8761)...
start "Eureka Server" cmd /k "cd /d %~dp0 && java -jar target/pr3-0.0.1-SNAPSHOT.jar --spring.profiles.active=eureka --spring.config.name=application-eureka"
echo ⏳ Ожидание запуска Eureka (15 секунд)...
timeout /t 15 /nobreak > nul

echo [3/3] Запуск Cinema Hub (порт 8081)...
start "Cinema Hub" cmd /k "cd /d %~dp0 && java -jar target/pr3-0.0.1-SNAPSHOT.jar --spring.profiles.active=microservice --spring.config.name=application-microservice"
echo ⏳ Ожидание запуска Cinema Hub (20 секунд)...
timeout /t 20 /nobreak > nul

echo.
echo ========================================
echo   ✅ Сервисы запущены!
echo ========================================
echo.
echo 📋 Доступные URL:
echo   • Cinema Hub:         http://localhost:8081
echo   • Eureka Dashboard:   http://localhost:8761
echo   • Health Check:       http://localhost:8081/actuator/health
echo.
echo 💡 Для запуска всех микросервисов используйте:
echo   start-all.bat
echo.
echo Нажмите любую клавишу для продолжения...
pause > nul






