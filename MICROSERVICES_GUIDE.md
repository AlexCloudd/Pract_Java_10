# 🔧 Руководство по работе с микросервисами

## ⚠️ Важно: Микросервисы - это REST API!

Микросервисы **НЕ имеют веб-страниц** на корневом пути. Они работают через **API endpoints**.

### ❌ Неправильно:
- http://localhost:8082/ ❌ (404 - страница не найдена)
- http://localhost:8083/ ❌ (404 - страница не найдена)
- http://localhost:8084/ ❌ (404 - страница не найдена)

### ✅ Правильно:
- http://localhost:8082/api/users ✅
- http://localhost:8083/api/movies ✅
- http://localhost:8084/api/ratings ✅

---

## 📋 Правильные URL для доступа к микросервисам

### 1. User Service (порт 8082)

**API Endpoints:**
- `GET http://localhost:8082/api/users` - получить всех пользователей
- `GET http://localhost:8082/api/users/{id}` - получить пользователя по ID
- `GET http://localhost:8082/api/users/username/{username}` - получить по username
- `POST http://localhost:8082/api/users` - создать пользователя
- `PUT http://localhost:8082/api/users/{id}` - обновить пользователя
- `DELETE http://localhost:8082/api/users/{id}` - удалить пользователя

**Проверка здоровья:**
- `GET http://localhost:8082/actuator/health` - статус сервиса

**Swagger UI:**
- `http://localhost:8082/swagger-ui.html` - документация API

---

### 2. Movie Service (порт 8083)

**API Endpoints:**
- `GET http://localhost:8083/api/movies` - получить все фильмы
- `GET http://localhost:8083/api/movies/{id}` - получить фильм по ID
- `GET http://localhost:8083/api/movies/tmdb/{tmdbId}` - получить по TMDB ID
- `POST http://localhost:8083/api/movies` - создать фильм
- `PUT http://localhost:8083/api/movies/{id}` - обновить фильм
- `DELETE http://localhost:8083/api/movies/{id}` - удалить фильм

**Проверка здоровья:**
- `GET http://localhost:8083/actuator/health` - статус сервиса

---

### 3. Rating Service (порт 8084)

**API Endpoints:**
- `GET http://localhost:8084/api/ratings` - получить все рейтинги
- `GET http://localhost:8084/api/ratings/{id}` - получить рейтинг по ID
- `GET http://localhost:8084/api/ratings/user/{userId}` - рейтинги пользователя
- `GET http://localhost:8084/api/ratings/movie/{movieId}` - рейтинги фильма
- `GET http://localhost:8084/api/ratings/movie/{movieId}/average` - средний рейтинг
- `POST http://localhost:8084/api/ratings` - создать рейтинг
- `PUT http://localhost:8084/api/ratings/{id}` - обновить рейтинг
- `DELETE http://localhost:8084/api/ratings/{id}` - удалить рейтинг

**Проверка здоровья:**
- `GET http://localhost:8084/actuator/health` - статус сервиса

---

### 4. API Gateway (порт 8080)

**Через Gateway (рекомендуется):**
- `http://localhost:8080/api/users` - User Service через Gateway
- `http://localhost:8080/api/movies` - Movie Service через Gateway
- `http://localhost:8080/api/ratings` - Rating Service через Gateway

**Swagger UI:**
- `http://localhost:8080/swagger-ui.html` - документация всех API

**Проверка здоровья:**
- `http://localhost:8080/actuator/health` - статус Gateway

---

### 5. Eureka Dashboard (порт 8761)

- `http://localhost:8761` - панель управления микросервисами
- Показывает все зарегистрированные сервисы

---

## 🚀 Как запустить все микросервисы

### Вариант 1: Автоматический запуск (рекомендуется)

```cmd
cd Pr_9
start-all.bat
```

Этот скрипт запустит все сервисы в правильном порядке:
1. Eureka Server (8761)
2. Config Server (8888)
3. API Gateway (8080)
4. Cinema Hub (8081)
5. User Service (8082)
6. Movie Service (8083)
7. Rating Service (8084)

### Вариант 2: Ручной запуск

Откройте отдельные окна командной строки для каждого сервиса:

```cmd
# Окно 1: Eureka Server
cd Pr_9
start-eureka.bat

# Окно 2: API Gateway
cd Pr_9
start-gateway.bat

# Окно 3: User Service
cd Pr_9\user-service
mvn spring-boot:run

# Окно 4: Movie Service
cd Pr_9\movie-service
mvn spring-boot:run

# Окно 5: Rating Service
cd Pr_9
start-rating-service.bat
```

---

## 🔍 Как проверить, что сервисы работают

### 1. Проверка через браузер

Откройте в браузере:
- `http://localhost:8082/actuator/health` - должен вернуть `{"status":"UP"}`
- `http://localhost:8083/actuator/health` - должен вернуть `{"status":"UP"}`
- `http://localhost:8084/actuator/health` - должен вернуть `{"status":"UP"}`

### 2. Проверка через Eureka

Откройте `http://localhost:8761` и проверьте список зарегистрированных сервисов.

### 3. Проверка через API

Используйте Postman, curl или браузер:
```bash
# Проверка User Service
curl http://localhost:8082/api/users

# Проверка Movie Service
curl http://localhost:8083/api/movies

# Проверка Rating Service
curl http://localhost:8084/api/ratings
```

---

## 🐛 Решение проблем

### Проблема: "404 - страница не найдена" на корневом пути

**Решение:** Это нормально! Микросервисы не имеют веб-страниц. Используйте API endpoints:
- Вместо `http://localhost:8082/` используйте `http://localhost:8082/api/users`
- Вместо `http://localhost:8083/` используйте `http://localhost:8083/api/movies`

### Проблема: Сервисы не запускаются

**Решение:**
1. Убедитесь, что PostgreSQL запущен
2. Проверьте, что база данных `Java_pr` создана
3. Убедитесь, что порты свободны (8080-8084, 8761, 8888)
4. Запустите Eureka Server первым

### Проблема: Сервисы не регистрируются в Eureka

**Решение:**
1. Убедитесь, что Eureka Server запущен на порту 8761
2. Подождите 30-60 секунд для регистрации
3. Проверьте Eureka Dashboard: http://localhost:8761

---

## 📝 Примеры использования API

### Получить всех пользователей:
```
GET http://localhost:8082/api/users
```

### Получить все фильмы:
```
GET http://localhost:8083/api/movies
```

### Получить рейтинги фильма:
```
GET http://localhost:8084/api/ratings/movie/1
```

### Через API Gateway:
```
GET http://localhost:8080/api/users
GET http://localhost:8080/api/movies
GET http://localhost:8080/api/ratings
```

---

## 💡 Полезные советы

1. **Используйте Swagger UI** для тестирования API:
   - http://localhost:8080/swagger-ui.html (через Gateway)
   - http://localhost:8082/swagger-ui.html (User Service)
   - http://localhost:8084/swagger-ui.html (Rating Service)

2. **Проверяйте логи** в консольных окнах каждого сервиса

3. **Используйте Eureka Dashboard** для мониторинга всех сервисов

4. **Для веб-интерфейса** используйте Cinema Hub на порту 8081:
   - http://localhost:8081 - главная страница






