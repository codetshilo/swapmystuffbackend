package com.swapmystuffbackend.controller

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/swaps")
class LoginController {

    private val logger = LoggerFactory.getLogger(LoginController::class.java)

    @PostMapping("/login")
    fun notifyLogin(authentication: Authentication): ResponseEntity<Unit> {
        val jwt = authentication as JwtAuthenticationToken
        logger.info("User ${jwt.name} logged in at ${System.currentTimeMillis()}")
        return ResponseEntity.ok().build()
    }
}