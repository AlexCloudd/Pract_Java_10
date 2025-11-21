# Rating Service

Микросервис для управления рейтингами фильмов в Cinema Hub.

## Описание

Rating Service предоставляет REST API для:
- Создания и обновления рейтингов фильмов
- Получения рейтингов по пользователям и фильмам
- Вычисления средних рейтингов
- Получения топ фильмов по рейтингу
- Управления отзывами

## Порт

- **8084** - основной порт сервиса

## API Endpoints

### Основные операции с рейтингами

- `POST /api/ratings` - создать новый рейтинг
- `PUT /api/ratings/{ratingId}` - обновить рейтинг
- `GET /api/ratings/{ratingId}` - получить рейтинг по ID
- `DELETE /api/ratings/{ratingId}` - удалить рейтинг

### Рейтинги по пользователям

- `GET /api/ratings/user/{userId}` - все рейтинги пользователя
- `GET /api/ratings/user/{userId}/movie/{movieId}` - рейтинг пользователя для фильма
- `GET /api/ratings/user/{userId}/reviews` - рейтинги пользователя с отзывами
- `GET /api/ratings/user/{userId}/range?minRating=X&maxRating=Y` - рейтинги пользователя по диапазону

### Рейтинги по фильмам

- `GET /api/ratings/movie/{movieId}` - все рейтинги фильма
- `GET /api/ratings/movie/{movieId}/average` - средний рейтинг фильма
- `GET /api/ratings/movie/{movieId}/count` - количество рейтингов фильма
- `GET /api/ratings/movie/{movieId}/reviews` - рейтинги фильма с отзывами
- `GET /api/ratings/movie/{movieId}/range?minRating=X&maxRating=Y` - рейтинги фильма по диапазону

### Аналитика

- `GET /api/ratings/top-rated?minRatings=5` - топ фильмов по рейтингу
- `GET /api/ratings/reviews` - все рейтинги с отзывами
- `GET /api/ratings/range?minRating=X&maxRating=Y` - рейтинги по диапазону

## Модель данных

### Rating Entity

```java
- id: Long (автоинкремент)
- userId: Long (ID пользователя)
- movieId: Long (ID фильма)
- rating: Integer (оценка от 1 до 10)
- review: String (отзыв, опционально)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

### RatingRequest DTO

```json
{
  "userId": 1,
  "movieId": 1,
  "rating": 8,
  "review": "Отличный фильм!"
}
```

### RatingResponse DTO

```json
{
  "id": 1,
  "userId": 1,
  "movieId": 1,
  "rating": 8,
  "review": "Отличный фильм!",
  "createdAt": "2024-01-01T12:00:00",
  "updatedAt": "2024-01-01T12:00:00"
}
```

## Запуск

1. Убедитесь, что PostgreSQL запущен
2. Убедитесь, что Eureka Server запущен
3. Запустите сервис:
   ```bash
   ./start-rating-service.bat
   ```

## Swagger UI

После запуска сервиса доступен по адресу:
- http://localhost:8084/swagger-ui.html

## Интеграция с другими сервисами

Rating Service интегрируется с:
- **Eureka Server** - для регистрации и обнаружения сервисов
- **API Gateway** - для маршрутизации запросов
- **PostgreSQL** - для хранения данных

## Особенности

- Валидация данных (оценка от 1 до 10)
- Предотвращение дублирования рейтингов (один пользователь = один рейтинг на фильм)
- Автоматическое обновление времени изменения
- Поддержка отзывов
- Аналитические запросы для получения статистики
