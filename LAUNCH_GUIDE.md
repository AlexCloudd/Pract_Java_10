# Cinema Hub - Полный запуск всех сервисов

## 🚀 Быстрый запуск

### Windows
```cmd
# Запуск всех сервисов
start-all.bat

# Остановка всех сервисов
stop-all.bat
```

### macOS/Linux
```bash
# Запуск всех сервисов
./start-all.sh

# Остановка всех сервисов
./stop-all.sh
```

## 📋 Что запускается автоматически

| Сервис | Порт | Описание |
|--------|------|----------|
| Eureka Server | 8761 | Сервис регистрации и обнаружения |
| Config Server | 8888 | Централизованная конфигурация |
| API Gateway | 8080 | Главный шлюз приложения |
| Cinema Hub | 8081 | Основной веб-сервис |
| User Service | 8082 | Управление пользователями |
| Movie Service | 8083 | Управление фильмами |
| Rating Service | 8084 | Рейтинги и отзывы |

## 🌐 Доступные URL

- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **Cinema Hub**: http://localhost:8081
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health

## 🔧 Ручной запуск (если нужно)

### Windows
```cmd
# 1. Сборка проекта
build.bat

# 2. Запуск по порядку
start-eureka.bat
start-config.bat
start-gateway.bat
start-microservice.bat
start-rating-service.bat

# 3. Запуск отдельных сервисов
cd user-service && mvn spring-boot:run
cd movie-service && mvn spring-boot:run
```

### macOS/Linux
```bash
# 1. Сборка проекта
./mvnw clean package -DskipTests

# 2. Запуск по порядку
java -jar target/pr3-0.0.1-SNAPSHOT.jar --spring.profiles.active=eureka
java -jar target/pr3-0.0.1-SNAPSHOT.jar --spring.profiles.active=config
java -jar target/pr3-0.0.1-SNAPSHOT.jar --spring.profiles.active=gateway
java -jar target/pr3-0.0.1-SNAPSHOT.jar --spring.profiles.active=microservice

# 3. Запуск отдельных сервисов
cd user-service && ./mvnw spring-boot:run
cd movie-service && ./mvnw spring-boot:run
cd rating-service && ./mvnw spring-boot:run
```

## ⚠️ Требования

- Java 17+
- Maven 3.6+
- PostgreSQL (порт 5432)
- Свободные порты: 8761, 8888, 8080-8084

## 🐛 Troubleshooting

### Проблема: Порт уже используется
**Решение**: Используйте `stop-all.bat` или `./stop-all.sh` для остановки всех сервисов

### Проблема: Сервис не запускается
**Решение**: 
1. Проверьте логи в `/tmp/*.log` (macOS/Linux) или в консоли (Windows)
2. Убедитесь, что PostgreSQL запущен
3. Проверьте, что все порты свободны

### Проблема: Сервисы не регистрируются в Eureka
**Решение**:
1. Убедитесь, что Eureka Server запущен первым
2. Подождите 30-60 секунд для полной регистрации
3. Проверьте Eureka Dashboard: http://localhost:8761

## 📊 Мониторинг

- **Eureka Dashboard**: http://localhost:8761 - статус всех сервисов
- **Actuator Health**: http://localhost:8080/actuator/health - общее состояние
- **Swagger UI**: http://localhost:8080/swagger-ui.html - API документация

## 🎯 Особенности

- ✅ Автоматическая регистрация в Eureka
- ✅ Проверка доступности портов
- ✅ Цветной вывод в консоли
- ✅ Автоматическая остановка при Ctrl+C (macOS/Linux)
- ✅ Сохранение логов в файлы
- ✅ Graceful shutdown всех сервисов
