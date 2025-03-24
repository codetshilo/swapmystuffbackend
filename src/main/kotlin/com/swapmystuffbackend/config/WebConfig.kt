package com.swapmystuffbackend.config

import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WebConfig {

    @Bean
    fun tomcatCustomizer(): WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
        return WebServerFactoryCustomizer { factory ->
            factory.port = System.getenv("PORT")?.toInt() ?: 8443
        }
    }
}