package com.example.plugins

import com.example.routes.authRoutes
import com.example.routes.taskRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class ApiInfo(
    val success: Boolean,
    val message: String,
    val version: String,
    val authEndpoints: List<String>,
    val taskEndpoints: List<String>
)

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond(HttpStatusCode.OK, ApiInfo(
                success = true,
                message = "Ktor CRUD + Auth API",
                version = "1.0.0",
                authEndpoints = listOf(
                    "POST /api/auth/register - Регистрация",
                    "POST /api/auth/login    - Вход (получить токен)"
                ),
                taskEndpoints = listOf(
                    "GET    /api/tasks           - Все задачи",
                    "GET    /api/tasks?completed= - Фильтр по статусу",
                    "GET    /api/tasks?search=   - Поиск",
                    "GET    /api/tasks/{id}      - Задача по ID",
                    "POST   /api/tasks           - Создать",
                    "PUT    /api/tasks/{id}      - Обновить",
                    "DELETE /api/tasks/{id}      - Удалить"
                )
            ))
        }

        authRoutes()
        taskRoutes()
    }
}