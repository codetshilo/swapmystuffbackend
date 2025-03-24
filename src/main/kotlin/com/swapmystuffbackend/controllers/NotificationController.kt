package com.swapmystuffbackend.controllers

import com.swapmystuffbackend.notifications.NotificationRequest
import com.swapmystuffbackend.notifications.NotificationService
import com.swapmystuffbackend.notifications.NotificationHelper
import com.swapmystuffbackend.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val notificationService: NotificationService,
    private val userService: UserService,
    private val notificationHelper: NotificationHelper
) {

    @PostMapping("/send")
    fun sendNotification(@RequestBody notificationRequest: NotificationRequest): ResponseEntity<String> {
        val fcmToken = notificationRequest.to // Treat 'to' as the FCM token
        notificationService.sendNotification(
            fcmToken = fcmToken, // Updated from 'url'
            title = notificationRequest.notification.title,
            body = notificationRequest.notification.body
        )
        return ResponseEntity.ok("Notification sent")
    }

    @PostMapping("/token/notification")
    fun updateNotificationServiceToken(@RequestParam token: String, @RequestParam userId: String): ResponseEntity<String> {
        notificationService.updateFcmToken(token, userId)
        return ResponseEntity.ok("Token updated in notification service")
    }

    @PostMapping("/token/user")
    fun updateUserServiceToken(@RequestParam token: String, @RequestParam userId: String): ResponseEntity<String> {
        userService.updateFcmToken(userId, token) // userId is email
        return ResponseEntity.ok("Token updated in user service")
    }

    @PostMapping("/welcome")
    fun sendWelcomeNotification(@RequestParam userId: String): ResponseEntity<String> {
        val notificationData = notificationHelper.prepareWelcomeNotification()
        val userFcmToken = userService.getFcmTokenForUser(userId) // userId is email
        return if (userFcmToken != null) {
            notificationService.sendNotification( // Line 23: Fixed to use 'fcmToken'
                fcmToken = userFcmToken,
                title = notificationData.title,
                body = notificationData.body
            )
            ResponseEntity.ok("Welcome notification sent")
        } else {
            ResponseEntity.badRequest().body("User FCM token not found")
        }
    }
}