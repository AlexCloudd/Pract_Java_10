# Настройка базы данных для CinemaHub

## Автоматическая настройка

### Для Windows:
```bash
# Запустите скрипт настройки
setup_database.bat
```

### Для macOS/Linux:
```bash
# Сделайте скрипт исполняемым
chmod +x setup_database.sh

# Запустите скрипт настройки
./setup_database.sh
```

## Ручная настройка

### 1. Подключение к PostgreSQL
```bash
# Войдите в PostgreSQL как суперпользователь
psql -U postgres
```

### 2. Создание базы данных и пользователя
```sql
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
```

### 3. Проверка настройки
```sql
-- Проверка подключения
SELECT current_database(), current_user;

-- Выход из PostgreSQL
\q
```

## Данные для подключения

После настройки используйте следующие данные:

- **База данных**: `cinema_hub`
- **Пользователь**: `cinema_user`
- **Пароль**: `cinema_password`
- **Хост**: `localhost`
- **Порт**: `5432`
- **URL**: `jdbc:postgresql://localhost:5432/cinema_hub`

## Проверка подключения

После настройки базы данных запустите приложение:

```bash
mvn spring-boot:run
```

Приложение автоматически создаст необходимые таблицы при первом запуске благодаря настройке `spring.jpa.hibernate.ddl-auto=update`.

## Возможные проблемы

### 1. Ошибка подключения к PostgreSQL
- Убедитесь, что PostgreSQL запущен
- Проверьте, что порт 5432 свободен
- Проверьте правильность пароля для пользователя postgres

### 2. Ошибка прав доступа
- Убедитесь, что пользователь postgres имеет права на создание баз данных
- Проверьте, что все команды GRANT выполнены успешно

### 3. Ошибка "database does not exist"
- Убедитесь, что база данных cinema_hub создана
- Проверьте правильность имени базы данных в application.properties

## Дополнительные настройки

### Изменение пароля
Если хотите изменить пароль, выполните:
```sql
ALTER USER cinema_user WITH PASSWORD 'новый_пароль';
```

И обновите `application.properties`:
```properties
spring.datasource.password=новый_пароль
```

### Изменение имени базы данных
Если хотите изменить имя базы данных, создайте новую базу и обновите `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/новое_имя_базы
```






