package com.swapmystuffbackend.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/auth")
class AuthController {

    @PostMapping("/login")
    fun notifyLogin(principal: Principal): ResponseEntity<Unit> {
        // Log the user ID from the token for debugging
        println("Login notified for user: ${principal.name}")
        return ResponseEntity.ok().build()
    }
}