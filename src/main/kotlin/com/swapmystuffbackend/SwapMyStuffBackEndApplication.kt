package com.swapmystuffbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableConfigurationProperties
@EnableJpaRepositories("com.swapmystuffbackend.data.repositories")
@EntityScan("com.swapmystuffbackend.data.models")
class SwapMyStuffBackEndApplication

fun main(args: Array<String>) {
    runApplication<SwapMyStuffBackEndApplication>(*args)
}