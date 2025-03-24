package com.swapmystuffbackend.notifications

data class NotificationRequest(
    val to: String, // FCM token or topic
    val notification: NotificationMessage
)