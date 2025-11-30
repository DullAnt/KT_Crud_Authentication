# Ktor CRUD + JWT Authentication API

REST API сервер на Ktor с полным CRUD и JWT аутентификацией.

## Технологии

- **Ktor** 2.3.7 - веб-фреймворк
- **kotlinx.serialization** - JSON сериализация
- **JWT** - аутентификация
- **BCrypt** - хеширование паролей
- **Gradle Kotlin DSL** - система сборки

## Запуск

```bash
./gradlew run
```

Сервер запустится на http://localhost:8080

## API Endpoints

### Аутентификация (открытые)

| Метод | URL | Описание |
|-------|-----|----------|
| POST | /api/auth/register | Регистрация нового пользователя |
| POST | /api/auth/login | Вход (получить JWT токен) |

### Задачи (требуют JWT токен)

| Метод | URL | Описание |
|-------|-----|----------|
| GET | /api/tasks | Получить все задачи пользователя |
| GET | /api/tasks?completed=true | Фильтр по статусу (query) |
| GET | /api/tasks?search=текст | Поиск по тексту (query) |
| GET | /api/tasks/{id} | Получить задачу по ID (path) |
| POST | /api/tasks | Создать задачу |
| PUT | /api/tasks/{id} | Обновить задачу |
| DELETE | /api/tasks/{id} | Удалить задачу |

## Примеры запросов (PowerShell)

### 1. Регистрация
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" -Method POST -ContentType "application/json" -Body '{"username":"user1","email":"user1@test.com","password":"1234"}'
```

Ответ:
```json
{
    "success": true,
    "message": "Регистрация успешна",
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9..."
}
```

### 2. Вход
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -ContentType "application/json" -Body '{"username":"user1","password":"1234"}'
```

### 3. Создать задачу (с токеном)
```powershell
$token = "ВАШ_ТОКЕН"
$headers = @{ Authorization = "Bearer $token" }
Invoke-RestMethod -Uri "http://localhost:8080/api/tasks" -Method POST -ContentType "application/json" -Headers $headers -Body '{"title":"Новая задача","description":"Описание"}'
```

### 4. Получить все задачи
```powershell
$token = "ВАШ_ТОКЕН"
$headers = @{ Authorization = "Bearer $token" }
Invoke-RestMethod -Uri "http://localhost:8080/api/tasks" -Headers $headers
```

### 5. Обновить задачу (PUT)
```powershell
$token = "ВАШ_ТОКЕН"
$headers = @{ Authorization = "Bearer $token" }
Invoke-RestMethod -Uri "http://localhost:8080/api/tasks/1" -Method PUT -ContentType "application/json" -Headers $headers -Body '{"title":"Обновленная","description":"Новое описание","completed":true}'
```

### 6. Удалить задачу
```powershell
$token = "ВАШ_ТОКЕН"
$headers = @{ Authorization = "Bearer $token" }
Invoke-RestMethod -Uri "http://localhost:8080/api/tasks/1" -Method DELETE -Headers $headers
```

### 7. Фильтр по статусу (query параметр)
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/tasks?completed=true" -Headers $headers
```

### 8. Поиск (query параметр)
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/tasks?search=задача" -Headers $headers
```

## HTTP-коды ответов

| Код | Описание |
|-----|----------|
| 200 | OK - успешный запрос |
| 201 | Created - ресурс создан |
| 400 | Bad Request - некорректный запрос |
| 401 | Unauthorized - требуется авторизация |
| 404 | Not Found - ресурс не найден |
| 409 | Conflict - пользователь уже существует |
| 500 | Internal Server Error |

## Структура проекта

```
src/main/kotlin/com/example/
├── Application.kt           # Точка входа
├── models/
│   └── Models.kt            # Data классы (@Serializable)
├── plugins/
│   ├── Plugins.kt           # Serialization, JWT, StatusPages
│   └── Routing.kt           # Конфигурация маршрутов
├── repository/
│   ├── UserRepository.kt    # Хранилище пользователей
│   └── TaskRepository.kt    # CRUD для задач
├── routes/
│   ├── AuthRoutes.kt        # /api/auth/*
│   └── TaskRoutes.kt        # /api/tasks/* (защищены JWT)
└── security/
    └── JwtConfig.kt         # JWT конфигурация
```

## Особенности

- Пароли хешируются через BCrypt
- JWT токен действителен 24 часа
- Каждый пользователь видит только свои задачи
- Полный CRUD: Create, Read, Update, Delete
