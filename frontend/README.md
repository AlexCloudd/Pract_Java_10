# Frontend Static Server

Простой Node.js сервер для статического хостинга HTML файлов.

## Настройка на Railway

1. **Root Directory**: укажите `frontend`
2. Railway автоматически определит Node.js проект по `package.json`
3. Сервер запустится на порту, указанном в переменной окружения `PORT`

## Структура маршрутов

- `/` → index.html
- `/movies` → movies.html
- `/movies/list` → movie-list.html
- `/movies/:id` → movie-detail.html
- `/music` → music.html
- `/watchlists` → watchlists.html
- `/watchlist/:id` → watchlist.html
- `/auth/login` → auth/login.html
- `/auth/register` → auth/register.html
- `/admin` → admin/dashboard.html
- `/admin/movies` → admin/movies/list.html
- `/admin/movies/add` → admin/movies/add.html
- `/admin/users` → admin/users/list.html
- `/admin/users/add` → admin/users/add.html
- `/api-docs` → api-docs.html

## Статические файлы

- `/css/*` → src/main/resources/static/css/*
- `/images/*` → src/main/resources/static/images/*

