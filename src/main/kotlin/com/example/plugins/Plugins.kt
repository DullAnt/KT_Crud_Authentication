package com.example.plugins

import com.example.models.ErrorResponse
import com.example.security.JwtConfig
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
}

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("jwt-auth") {
            realm = JwtConfig.realm
            verifier(
                com.auth0.jwt.JWT
                    .require(JwtConfig.algorithm)
                    .withIssuer(JwtConfig.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("userId").asInt() != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse(
                        error = "Требуется авторизация. Добавьте заголовок: Authorization: Bearer <token>",
                        code = 401
                    )
                )
            }
        }
    }
}

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    error = cause.message ?: "Внутренняя ошибка сервера",
                    code = 500
                )
            )
        }
        
        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(
                status,
                ErrorResponse(
                    error = "Ресурс не найден",
                    code = 404
                )
            )
        }
    }
}
