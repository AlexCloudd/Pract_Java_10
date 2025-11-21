-- CinemaHub Database Setup Script
-- Выполните этот скрипт в PostgreSQL для настройки базы данных

-- Создание базы данных
CREATE DATABASE cinema_hub;

-- Создание пользователя
CREATE USER cinema_user WITH PASSWORD 'cinema_password';

-- Предоставление прав доступа
GRANT ALL PRIVILEGES ON DATABASE cinema_hub TO cinema_user;

-- Подключение к базе данных
\c cinema_hub;

-- Предоставление прав на схему public
GRANT ALL ON SCHEMA public TO cinema_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO cinema_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO cinema_user;

-- Установка прав по умолчанию для новых таблиц
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cinema_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cinema_user;

-- Проверка создания
SELECT current_database(), current_user;

-- Информация о созданной базе данных
\l cinema_hub






