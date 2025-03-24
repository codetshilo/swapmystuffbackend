package com.swapmystuffbackend.config

import org.springframework.context.annotation.Configuration

@Configuration
class ApiConfig {
    var baseUrl: String = "https://192.168.9.138:8443/api" // Updated to current IP and HTTPS port

    fun fetchBaseUrl(): String = baseUrl
    fun updateBaseUrl(newBaseUrl: String) {
        baseUrl = newBaseUrl
    }
}