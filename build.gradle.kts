plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    application
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Ktor Server
    implementation("io.ktor:ktor-server-core:2.3.7")
    implementation("io.ktor:ktor-server-netty:2.3.7")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-server-status-pages:2.3.7")
    
    // JWT Authentication
    implementation("io.ktor:ktor-server-auth:2.3.7")
    implementation("io.ktor:ktor-server-auth-jwt:2.3.7")
    
    // JSON Serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    
    // Password hashing
    implementation("org.mindrot:jbcrypt:0.4")
    
    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")
}

application {
    mainClass.set("com.example.ApplicationKt")
}

kotlin {
    jvmToolchain(21)
}
