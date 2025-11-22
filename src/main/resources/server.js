const express = require('express');
const path = require('path');
const fs = require('fs');

const app = express();
const PORT = process.env.PORT || 3000;

// Логируем информацию о запуске
console.log('========================================');
console.log('Starting Frontend Server...');
console.log(`PORT: ${PORT}`);
console.log(`NODE_ENV: ${process.env.NODE_ENV || 'not set'}`);
console.log(`__dirname: ${__dirname}`);
console.log('========================================');

// Проверяем существование директорий
const staticPath = path.join(__dirname, 'static');
const templatesPath = path.join(__dirname, 'templates');

console.log(`Checking directories...`);
console.log(`  static: ${staticPath} - ${fs.existsSync(staticPath) ? 'EXISTS' : 'NOT FOUND'}`);
console.log(`  templates: ${templatesPath} - ${fs.existsSync(templatesPath) ? 'EXISTS' : 'NOT FOUND'}`);

// Статические файлы (CSS, изображения)
// Пути относительно текущей директории (src/main/resources)
app.use('/css', express.static(path.join(__dirname, 'static', 'css')));
app.use('/images', express.static(path.join(__dirname, 'static', 'images')));
app.use(express.static(path.join(__dirname, 'static')));

// Middleware для логирования запросов
app.use((req, res, next) => {
    console.log(`${new Date().toISOString()} - ${req.method} ${req.path}`);
    next();
});

// Маршруты для HTML страниц
app.get('/', (req, res) => {
    const filePath = path.join(templatesPath, 'index.html');
    if (fs.existsSync(filePath)) {
        res.sendFile(filePath);
    } else {
        console.error(`File not found: ${filePath}`);
        res.status(404).send('File not found');
    }
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
    const filePath = path.join(templatesPath, 'auth', 'login.html');
    if (fs.existsSync(filePath)) {
        res.sendFile(filePath);
    } else {
        console.error(`File not found: ${filePath}`);
        res.status(404).send('Login page not found');
    }
});

app.get('/auth/register', (req, res) => {
    const filePath = path.join(templatesPath, 'auth', 'register.html');
    if (fs.existsSync(filePath)) {
        res.sendFile(filePath);
    } else {
        console.error(`File not found: ${filePath}`);
        res.status(404).send('Register page not found');
    }
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

// Запускаем сервер
app.listen(PORT, '0.0.0.0', () => {
    console.log('========================================');
    console.log(`✅ Frontend server running on port ${PORT} (0.0.0.0)`);
    console.log(`✅ Server is ready to accept connections`);
    console.log(`✅ Access the server at: http://0.0.0.0:${PORT}`);
    console.log('========================================');
}).on('error', (err) => {
    console.error('========================================');
    console.error('❌ Failed to start server:');
    console.error(err);
    console.error('========================================');
    process.exit(1);
});

