# Руководство по запуску MusicCloud

## Быстрый старт

### 1. Подготовка
```bash
# Сборка проекта
mvn clean package -DskipTests

# Или используйте скрипт
build.bat
```

### 2. Запуск компонентов (в порядке)

#### Шаг 1: Eureka Server (Порт 8761)
```bash
# Через Maven
mvn spring-boot:run -Dspring-boot.run.profiles=eureka

# Или через JAR
java -jar target/pr3-0.0.1-SNAPSHOT.jar --spring.profiles.active=eureka

# Или используйте скрипт
start-eureka.bat
```

**Проверка**: http://localhost:8761

#### Шаг 2: Config Server (Порт 8888)
```bash
# Через Maven
mvn spring-boot:run -Dspring-boot.run.profiles=config

# Или через JAR
java -jar target/pr3-0.0.1-SNAPSHOT.jar --spring.profiles.active=config
```

**Проверка**: http://localhost:8888/actuator/health

#### Шаг 3: API Gateway (Порт 8080)
```bash
# Через Maven
mvn spring-boot:run -Dspring-boot.run.profiles=gateway

# Или через JAR
java -jar target/pr3-0.0.1-SNAPSHOT.jar --spring.profiles.active=gateway

# Или используйте скрипт
start-gateway.bat
```

**Проверка**: http://localhost:8080

#### Шаг 4: Микросервисы
```bash
# Через Maven
mvn spring-boot:run -Dspring-boot.run.profiles=microservice

# Или через JAR
java -jar target/pr3-0.0.1-SNAPSHOT.jar --spring.profiles.active=microservice

# Или используйте скрипт
start-microservice.bat
```

## Проверка работы

### 1. Eureka Dashboard
- URL: http://localhost:8761
- Должны быть зарегистрированы сервисы:
  - CONFIG-SERVER
  - API-GATEWAY
  - MUSIC-CLOUD-SERVICE

### 2. API Gateway
- URL: http://localhost:8080
- Главная страница приложения

### 3. API Документация
- URL: http://localhost:8080/swagger-ui.html
- Swagger UI с документацией всех API

### 4. Мониторинг
- URL: http://localhost:8080/actuator
- Метрики и состояние приложения

## Порты

| Сервис | Порт | Описание |
|--------|------|----------|
| Eureka Server | 8761 | Сервис регистрации |
| Config Server | 8888 | Централизованная конфигурация |
| API Gateway | 8080 | Главный сервис |
| PostgreSQL | 5432 | База данных |

## Troubleshooting

### Проблема: Сервис не регистрируется в Eureka
**Решение:**
1. Проверьте, что Eureka Server запущен первым
2. Убедитесь, что порт 8761 свободен
3. Проверьте настройки в application.yml

### Проблема: Ошибка подключения к Config Server
**Решение:**
1. Убедитесь, что Config Server запущен
2. Проверьте URL в настройках: `http://localhost:8888`
3. Проверьте, что порт 8888 свободен

### Проблема: Ошибка базы данных
**Решение:**
1. Убедитесь, что PostgreSQL запущен
2. Проверьте настройки подключения в application.properties
3. Создайте базу данных и пользователя

### Проблема: Audius API не работает
**Решение:**
1. Проверьте интернет-соединение
2. Circuit Breaker автоматически переключится на fallback
3. Проверьте логи для деталей ошибки

## Логи

Логи доступны в консоли и через Actuator:
- http://localhost:8080/actuator/loggers
- http://localhost:8080/actuator/logfile

## Остановка

Для остановки всех сервисов:
1. Нажмите `Ctrl+C` в каждой консоли
2. Или закройте окна консоли

## Перезапуск

Для перезапуска:
1. Остановите все сервисы
2. Подождите 10-15 секунд
3. Запустите в том же порядке

## Профили Spring

- `eureka` - Eureka Server
- `config` - Config Server  
- `gateway` - API Gateway
- `microservice` - Микросервисы

## Дополнительные команды

### Проверка состояния
```bash
# Проверка Eureka
curl http://localhost:8761/eureka/apps

# Проверка Config Server
curl http://localhost:8888/actuator/health

# Проверка API Gateway
curl http://localhost:8080/actuator/health
```

### Очистка базы данных
```sql
-- Подключитесь к PostgreSQL
DROP DATABASE IF EXISTS music_cloud;
CREATE DATABASE music_cloud;
```
