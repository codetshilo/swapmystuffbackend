package com.swapmystuffbackend.data.models

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var id: Long? = null,
    @Column(name = "name")
    val name: String? = null,
    @Column(name = "username", unique = true)
    val username: String? = null,
    @Column(name = "email", unique = true)
    val email: String? = null,
    @Column(name = "fcm_token")
    var fcmToken: String? = null
)