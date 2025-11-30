package com.example.routes

import com.example.models.AuthResponse
import com.example.models.LoginRequest
import com.example.models.RegisterRequest
import com.example.repository.UserRepository
import com.example.security.JwtConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {
    route("/api/auth") {
        
        // POST /api/auth/register - регистрация
        post("/register") {
            try {
                val request = call.receive<RegisterRequest>()
                
                if (request.username.isBlank() || request.password.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, AuthResponse(
                        success = false,
                        message = "Username и password обязательны"
                    ))
                    return@post
                }
                
                if (request.password.length < 4) {
                    call.respond(HttpStatusCode.BadRequest, AuthResponse(
                        success = false,
                        message = "Пароль должен быть минимум 4 символа"
                    ))
                    return@post
                }
                
                val user = UserRepository.create(request.username, request.email, request.password)
                
                if (user == null) {
                    call.respond(HttpStatusCode.Conflict, AuthResponse(
                        success = false,
                        message = "Пользователь с таким username уже существует"
                    ))
                    return@post
                }
                
                val token = JwtConfig.generateToken(user.id, user.username)
                
                call.respond(HttpStatusCode.Created, AuthResponse(
                    success = true,
                    message = "Регистрация успешна",
                    token = token
                ))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, AuthResponse(
                    success = false,
                    message = "Ошибка: ${e.message}"
                ))
            }
        }
        
        // POST /api/auth/login - вход
        post("/login") {
            try {
                val request = call.receive<LoginRequest>()
                
                val user = UserRepository.findByUsername(request.username)
                
                if (user == null || !UserRepository.validatePassword(user, request.password)) {
                    call.respond(HttpStatusCode.Unauthorized, AuthResponse(
                        success = false,
                        message = "Неверный username или password"
                    ))
                    return@post
                }
                
                val token = JwtConfig.generateToken(user.id, user.username)
                
                call.respond(HttpStatusCode.OK, AuthResponse(
                    success = true,
                    message = "Вход выполнен",
                    token = token
                ))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, AuthResponse(
                    success = false,
                    message = "Ошибка: ${e.message}"
                ))
            }
        }
    }
}
