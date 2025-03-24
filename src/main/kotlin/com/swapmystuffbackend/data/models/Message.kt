package com.swapmystuffbackend.data.models

import jakarta.persistence.*

@Entity
@Table(name = "messages")
data class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "swap_id")
    val swapId: String,
    val senderId: String,
    val receiverId: String,
    val messageText: String,
    val timestamp: Long
)