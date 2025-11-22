# Railway Database Setup Guide

## Проблема: Connection refused to localhost:5432

Если вы видите ошибку `Connection to localhost:5432 refused`, это означает, что переменные окружения базы данных не установлены в Railway.

## Решение: Связать базу данных с сервисом приложения

### Шаг 1: Убедитесь, что база данных создана в Railway

1. В Railway Dashboard создайте новый сервис PostgreSQL (если еще не создан)
2. Дождитесь полной инициализации базы данных

### Шаг 2: Свяжите базу данных с сервисом приложения

1. Откройте ваш сервис приложения (`cinema-hub-service`) в Railway Dashboard
2. Перейдите на вкладку **"Variables"** (Переменные)
3. Нажмите **"New Variable"** или **"Add from Service"**
4. Выберите ваш сервис PostgreSQL из списка
5. Railway автоматически добавит следующие переменные:
   - `DATABASE_URL` - полный URL подключения
   - `PGHOST` - хост базы данных
   - `PGPORT` - порт (обычно 5432)
   - `PGDATABASE` - имя базы данных
   - `PGUSER` - имя пользователя
   - `PGPASSWORD` - пароль

### Шаг 3: Перезапустите сервис

После связывания базы данных:
1. Перезапустите сервис приложения
2. Проверьте логи - вы должны увидеть:
   - `Found DATABASE_URL, converting to JDBC format...`
   - `Successfully configured datasource from DATABASE_URL`
   - Успешное подключение к базе данных

## Альтернативный способ: Ручная настройка переменных

Если автоматическое связывание не работает, вы можете вручную установить переменные:

1. В Railway Dashboard откройте ваш сервис приложения
2. Перейдите на вкладку **"Variables"**
3. Добавьте следующие переменные:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://<PGHOST>:<PGPORT>/<PGDATABASE>
SPRING_DATASOURCE_USERNAME=<PGUSER>
SPRING_DATASOURCE_PASSWORD=<PGPASSWORD>
```

Где:
- `<PGHOST>` - хост из вашего PostgreSQL сервиса (обычно что-то вроде `containers-us-west-xxx.railway.app`)
- `<PGPORT>` - порт (обычно `5432`)
- `<PGDATABASE>` - имя базы данных (обычно `railway`)
- `<PGUSER>` - имя пользователя (обычно `postgres`)
- `<PGPASSWORD>` - пароль (можно найти в настройках PostgreSQL сервиса)

## Проверка

После настройки проверьте логи запуска. Вы должны увидеть:

```
Found DATABASE_URL, converting to JDBC format...
Converted DATABASE_URL to JDBC format: jdbc:postgresql://host:port/database
Extracted username from DATABASE_URL
Extracted password from DATABASE_URL
Successfully configured datasource from DATABASE_URL
```

Или, если используются отдельные переменные:

```
Checking Railway PostgreSQL variables:
  PGHOST: <host>
  PGPORT: 5432
  PGDATABASE: <database>
  PGUSER: <user>
  PGPASSWORD: ***
```

## Важные замечания

1. **Не используйте localhost** - в Railway каждый сервис работает в отдельном контейнере, поэтому `localhost` не будет работать
2. **Используйте переменные окружения** - Railway автоматически предоставляет правильные значения при связывании сервисов
3. **Проверьте логи** - если переменные не установлены, вы увидите предупреждение с инструкциями

## Дополнительная информация

Приложение автоматически:
- Конвертирует `DATABASE_URL` из формата Railway в JDBC формат
- Использует переменные `PGHOST`, `PGPORT`, `PGDATABASE`, `PGUSER`, `PGPASSWORD` если `DATABASE_URL` не установлен
- Логирует все переменные окружения для отладки


