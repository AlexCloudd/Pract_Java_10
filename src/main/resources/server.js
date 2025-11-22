const express = require('express');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;

// Статические файлы (CSS, изображения)
// Пути относительно текущей директории (src/main/resources)
app.use('/css', express.static(path.join(__dirname, 'static', 'css')));
app.use('/images', express.static(path.join(__dirname, 'static', 'images')));
app.use(express.static(path.join(__dirname, 'static')));

// Путь к HTML шаблонам
const templatesPath = path.join(__dirname, 'templates');

// Маршруты для HTML страниц
app.get('/', (req, res) => {
    res.sendFile(path.join(templatesPath, 'index.html'));
});

app.get('/movies', (req, res) => {
    res.sendFile(path.join(templatesPath, 'movies.html'));
});

app.get('/movies/list', (req, res) => {
    res.sendFile(path.join(templatesPath, 'movie-list.html'));
});

app.get('/movies/:id', (req, res) => {
    res.sendFile(path.join(templatesPath, 'movie-detail.html'));
});

app.get('/music', (req, res) => {
    res.sendFile(path.join(templatesPath, 'music.html'));
});

app.get('/watchlists', (req, res) => {
    res.sendFile(path.join(templatesPath, 'watchlists.html'));
});

app.get('/watchlist/:id', (req, res) => {
    res.sendFile(path.join(templatesPath, 'watchlist.html'));
});

// Авторизация
app.get('/auth/login', (req, res) => {
    res.sendFile(path.join(templatesPath, 'auth', 'login.html'));
});

app.get('/auth/register', (req, res) => {
    res.sendFile(path.join(templatesPath, 'auth', 'register.html'));
});

// Админ панель
app.get('/admin', (req, res) => {
    res.sendFile(path.join(templatesPath, 'admin', 'dashboard.html'));
});

app.get('/admin/movies', (req, res) => {
    res.sendFile(path.join(templatesPath, 'admin', 'movies', 'list.html'));
});

app.get('/admin/movies/add', (req, res) => {
    res.sendFile(path.join(templatesPath, 'admin', 'movies', 'add.html'));
});

app.get('/admin/users', (req, res) => {
    res.sendFile(path.join(templatesPath, 'admin', 'users', 'list.html'));
});

app.get('/admin/users/add', (req, res) => {
    res.sendFile(path.join(templatesPath, 'admin', 'users', 'add.html'));
});

// API документация
app.get('/api-docs', (req, res) => {
    res.sendFile(path.join(templatesPath, 'api-docs.html'));
});

// Обработка 404
app.use((req, res) => {
    res.status(404).sendFile(path.join(templatesPath, 'index.html'));
});

app.listen(PORT, () => {
    console.log(`Frontend server running on port ${PORT}`);
});

