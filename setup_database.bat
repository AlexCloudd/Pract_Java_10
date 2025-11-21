@echo off
echo ========================================
echo    CinemaHub Database Setup Script
echo ========================================
echo.

echo Проверка подключения к PostgreSQL...
psql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ОШИБКА: PostgreSQL не найден в PATH!
    echo Убедитесь, что PostgreSQL установлен и добавлен в PATH
    pause
    exit /b 1
)

echo PostgreSQL найден. Начинаем настройку базы данных...
echo.

echo Создание базы данных cinema_hub...
psql -U postgres -c "CREATE DATABASE cinema_hub;" 2>nul
if %errorlevel% neq 0 (
    echo База данных уже существует или произошла ошибка
)

echo Создание пользователя cinema_user...
psql -U postgres -c "CREATE USER cinema_user WITH PASSWORD 'cinema_password';" 2>nul
if %errorlevel% neq 0 (
    echo Пользователь уже существует или произошла ошибка
)

echo Предоставление прав доступа...
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE cinema_hub TO cinema_user;"

echo Настройка прав в базе данных...
psql -U postgres -d cinema_hub -c "GRANT ALL ON SCHEMA public TO cinema_user;"
psql -U postgres -d cinema_hub -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO cinema_user;"
psql -U postgres -d cinema_hub -c "GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO cinema_user;"
psql -U postgres -d cinema_hub -c "ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cinema_user;"
psql -U postgres -d cinema_hub -c "ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cinema_user;"

echo.
echo ========================================
echo    База данных успешно настроена!
echo ========================================
echo.
echo Данные для подключения:
echo - База данных: cinema_hub
echo - Пользователь: cinema_user
echo - Пароль: cinema_password
echo - Хост: localhost
echo - Порт: 5432
echo.
echo Теперь можно запускать приложение!
echo.
pause






