package com.swapmystuffbackend.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.core.io.ClassPathResource
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class AppConfig {

    @Bean
    @Order(1)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/api/**") // Explicitly apply to /api/**
            .httpBasic { it.disable() }
            .cors { cors -> cors.configurationSource(corsConfigurationSource()) }
            .csrf { csrf ->
                csrf.disable()
                println("CSRF disabled in security config")
            }
            .sessionManagement { session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                println("Session management set to STATELESS")
            }
            .requestCache { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/public/**").permitAll()
                    .requestMatchers("/api/auth/login").authenticated()
                    .requestMatchers("/api/upload/**").authenticated()
                    .requestMatchers("/api/products/**").authenticated()
                    .requestMatchers("/api/error/**").permitAll()
                    .anyRequest().authenticated()
                    .and().also { println("Security rules applied") }
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2
                    .jwt { jwt -> 
                        jwt.decoder(jwtDecoder())
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                        println("OAuth2 JWT authentication configured")
                    }
                    .authenticationEntryPoint { _, response, authException ->
                        response.sendError(401, "Unauthorized: ${authException.message}")
                        println("Authentication failed: ${authException.message}")
                    }
            }
        return http.build().also { println("Security filter chain built for /api/**") }
    }

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setPrincipalClaimName("sub")
        println("JWT converter set with principal claim 'sub'")
        return converter
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val jwkSetUri = "https://www.googleapis.com/robot/v1/metadata/jwk/securetoken@system.gserviceaccount.com"
        val decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build()
        val validator = JwtValidators.createDefaultWithIssuer("https://securetoken.google.com/swapmystuff20")
        decoder.setJwtValidator(validator)
        println("JWT decoder initialized with Firebase JWK set: $jwkSetUri")
        return decoder
    }

    @Bean
    fun firebaseInitializer(): FirebaseApp {
        val serviceAccount = ClassPathResource("swapmystuff20-firebase-adminsdk-fbsvc-a2a23fb032.json").inputStream
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()
        val appName = "swapmystuffbackend"
        return try {
            FirebaseApp.getInstance(appName)
        } catch (e: IllegalStateException) {
            FirebaseApp.initializeApp(options, appName).also { println("Firebase initialized: $appName") }
        }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf(
            "http://localhost:8080",
            "https://192.168.9.138:8443"
        )
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        println("CORS configured with origins: ${configuration.allowedOrigins}")
        return source
    }
}