# Инструкция по развертыванию микросервисов на Railway

## Структура проекта

Каждый микросервис находится в отдельной папке:
- `movie-service/` - сервис фильмов
- `user-service/` - сервис пользователей
- `rating-service/` - сервис рейтингов
- `api-gateway/` - API Gateway
- `eureka-server/` - Service Discovery

## Настройка Railway для каждого микросервиса

### ⚠️ КРИТИЧЕСКИ ВАЖНО: Настройка Root Directory

**Проблема "There was an error deploying from source" обычно возникает из-за неправильно указанного Root Directory!**

### Пошаговая инструкция:

1. **Создайте новый сервис в Railway**
   - Нажмите "New Project" → "Deploy from GitHub repo"
   - Выберите репозиторий: `AlexCloudd/Pract_Java_10`

2. **Настройте Root Directory (ОБЯЗАТЕЛЬНО!):**
   - После создания сервиса, перейдите в **Settings** → **Source**
   - Найдите поле **Root Directory**
   - Укажите путь к папке микросервиса:
     - Для `movie-service`: введите `movie-service`
     - Для `user-service`: введите `user-service`
     - Для `rating-service`: введите `rating-service`
     - Для `api-gateway`: введите `api-gateway`
     - Для `eureka-server`: введите `eureka-server`
   - **Сохраните изменения**

3. **Проверьте настройки сборки:**
   - Перейдите в **Settings** → **Deploy**
   - **Build Command** должен быть: `mvn clean package -DskipTests`
   - **Start Command** должен быть (автоматически определится из Procfile):
     - `java -jar target/movie-service-0.0.1-SNAPSHOT.jar` (для movie-service)
     - `java -jar target/user-service-0.0.1-SNAPSHOT.jar` (для user-service)
     - `java -jar target/rating-service-0.0.1-SNAPSHOT.jar` (для rating-service)
     - `java -jar target/api-gateway-0.0.1-SNAPSHOT.jar` (для api-gateway)
     - `java -jar target/eureka-server-0.0.1-SNAPSHOT.jar` (для eureka-server)

4. **Build Command** (должен автоматически определиться):
   ```
   mvn clean package -DskipTests
   ```

5. **Start Command** (должен автоматически определиться):
   - Для `movie-service`: `java -jar target/movie-service-0.0.1-SNAPSHOT.jar`
   - Для `user-service`: `java -jar target/user-service-0.0.1-SNAPSHOT.jar`
   - Для `rating-service`: `java -jar target/rating-service-0.0.1-SNAPSHOT.jar`
   - Для `api-gateway`: `java -jar target/api-gateway-0.0.1-SNAPSHOT.jar`
   - Для `eureka-server`: `java -jar target/eureka-server-0.0.1-SNAPSHOT.jar`

## Настройка переменных окружения

### Для сервисов с базой данных (movie-service, user-service, rating-service):

**Railway автоматически предоставляет переменные окружения при подключении PostgreSQL:**
- `PGHOST` - хост базы данных
- `PGPORT` - порт базы данных
- `PGDATABASE` - имя базы данных
- `PGUSER` - пользователь
- `PGPASSWORD` - пароль

**Альтернативные переменные (также поддерживаются):**
- `DB_HOST` - хост базы данных
- `DB_PORT` - порт базы данных
- `DB_NAME` - имя базы данных
- `DB_USERNAME` - пользователь
- `DB_PASSWORD` - пароль

**Важно**: 
- Подключите PostgreSQL сервис к каждому микросервису, который использует базу данных
- Приложение автоматически использует переменные окружения, если они установлены
- Если переменные не установлены, используются значения по умолчанию для локальной разработки

### Автоматические переменные Railway:

Railway автоматически предоставляет:
- `PORT` - порт, на котором должен работать сервис (обязательно использовать!)

### Для Eureka Server:

Установите переменную окружения (опционально):
- `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE` - URL Eureka сервера (по умолчанию: `http://localhost:8761/eureka/`)

### Для микросервисов (movie-service, user-service, rating-service, api-gateway):

Установите переменные окружения:
- `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE` - URL Eureka сервера
  - Если Eureka развернут на Railway: `https://your-eureka-service.up.railway.app/eureka/`
  - Или используйте внутренний URL: `http://eureka-server:8761/eureka/`
  - По умолчанию: `http://localhost:8761/eureka/` (для локальной разработки)

### Для API Gateway:

Убедитесь, что все микросервисы зарегистрированы в Eureka, иначе Gateway не сможет их найти.

## Порядок развертывания

1. **Сначала разверните Eureka Server**
   - Дождитесь успешного запуска
   - Запишите его публичный URL

2. **Затем разверните базу данных PostgreSQL**
   - Создайте новый PostgreSQL сервис в Railway
   - Подключите его к микросервисам, которые используют БД

3. **Разверните микросервисы** (movie-service, user-service, rating-service)
   - Установите переменные окружения для Eureka
   - Подключите PostgreSQL сервис

4. **В последнюю очередь разверните API Gateway**
   - Установите переменные окружения для Eureka
   - Gateway автоматически найдет все зарегистрированные сервисы

## Проверка развертывания

1. Проверьте логи каждого сервиса на наличие ошибок
2. Убедитесь, что все сервисы зарегистрированы в Eureka
3. Проверьте health endpoints:
   - `https://your-service.up.railway.app/actuator/health`

## Решение проблем

### Ошибка "There was an error deploying from source"

1. Убедитесь, что указан правильный **Root Directory**
2. Проверьте, что в папке микросервиса есть `pom.xml`
3. Проверьте логи сборки на наличие ошибок компиляции

### Сервисы не могут подключиться к Eureka

1. Проверьте переменную окружения `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE`
2. Убедитесь, что Eureka Server запущен и доступен
3. Проверьте, что используется правильный URL (публичный или внутренний)

### Ошибки подключения к базе данных

1. Убедитесь, что PostgreSQL сервис подключен к микросервису
2. Проверьте, что переменные окружения установлены:
   - Railway автоматически устанавливает: `PGHOST`, `PGPORT`, `PGDATABASE`, `PGUSER`, `PGPASSWORD`
   - Или можно использовать альтернативные: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`
3. **Важно**: Если приложение пытается подключиться к `localhost:5432`, это означает, что переменные окружения не установлены
4. Проверьте логи на наличие ошибок подключения
5. Убедитесь, что в Railway настройках сервиса подключен PostgreSQL сервис

