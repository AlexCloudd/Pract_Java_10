#!/bin/bash

echo "========================================"
echo "   CinemaHub Database Setup Script"
echo "========================================"
echo

# Проверка подключения к PostgreSQL
if ! command -v psql &> /dev/null; then
    echo "ОШИБКА: PostgreSQL не найден!"
    echo "Убедитесь, что PostgreSQL установлен и добавлен в PATH"
    exit 1
fi

echo "PostgreSQL найден. Начинаем настройку базы данных..."
echo

# Создание базы данных
echo "Создание базы данных cinema_hub..."
psql -U postgres -c "CREATE DATABASE cinema_hub;" 2>/dev/null || echo "База данных уже существует или произошла ошибка"

# Создание пользователя
echo "Создание пользователя cinema_user..."
psql -U postgres -c "CREATE USER cinema_user WITH PASSWORD 'cinema_password';" 2>/dev/null || echo "Пользователь уже существует или произошла ошибка"

# Предоставление прав доступа
echo "Предоставление прав доступа..."
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE cinema_hub TO cinema_user;"

# Настройка прав в базе данных
echo "Настройка прав в базе данных..."
psql -U postgres -d cinema_hub -c "GRANT ALL ON SCHEMA public TO cinema_user;"
psql -U postgres -d cinema_hub -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO cinema_user;"
psql -U postgres -d cinema_hub -c "GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO cinema_user;"
psql -U postgres -d cinema_hub -c "ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cinema_user;"
psql -U postgres -d cinema_hub -c "ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cinema_user;"

echo
echo "========================================"
echo "   База данных успешно настроена!"
echo "========================================"
echo
echo "Данные для подключения:"
echo "- База данных: cinema_hub"
echo "- Пользователь: cinema_user"
echo "- Пароль: cinema_password"
echo "- Хост: localhost"
echo "- Порт: 5432"
echo
echo "Теперь можно запускать приложение!"
echo






