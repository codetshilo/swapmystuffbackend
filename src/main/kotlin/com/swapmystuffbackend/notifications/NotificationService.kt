package com.swapmystuffbackend.notifications

import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.swapmystuffbackend.services.UserService

@Service
class NotificationService @Autowired constructor(
    private val firebaseApp: FirebaseApp,
    private val userService: UserService
) {

    private val logger = LoggerFactory.getLogger(NotificationService::class.java)

    init {
        logger.info("NotificationService initialized with FirebaseApp: ${firebaseApp.name}")
    }

    fun sendSwapNotification(product1Name: String, product2Name: String, userId: String, swapWithUserId: String) {
        val message = NotificationMessage(
            title = "Swap Notification",
            body = "$product1Name swapped with $product2Name"
        )
        userService.getFcmTokenForUser(userId)?.let { token ->
            sendFcmNotification(token, message)
        }
        userService.getFcmTokenForUser(swapWithUserId)?.let { token ->
            sendFcmNotification(token, message)
        }
    }

    fun sendWelcomeNotification(userId: String) {
        val message = NotificationMessage(
            title = "Welcome",
            body = "Welcome to SwapMyStuff! Please feel free to create an account."
        )
        userService.getFcmTokenForUser(userId)?.let { token ->
            sendFcmNotification(token, message)
        }
    }

    fun sendMessageNotification(swapId: String, senderId: String, receiverId: String, messageText: String) {
        val message = NotificationMessage(
            title = "New Message in Swap $swapId",
            body = messageText
        )
        userService.getFcmTokenForUser(receiverId)?.let { token ->
            sendFcmNotification(token, message)
        }
    }

    fun sendNotification(fcmToken: String, title: String, body: String) {
        val message = NotificationMessage(title = title, body = body)
        sendFcmNotification(fcmToken, message)
    }

    fun updateFcmToken(token: String, userId: String) {
        userService.updateFcmToken(userId, token)?.let {
            logger.info("Updated FCM token for user $userId: $token")
        } ?: logger.warn("Failed to update FCM token for user $userId: User not found")
    }

    private fun sendFcmNotification(fcmToken: String, message: NotificationMessage) {
        try {
            val fcmMessage = Message.builder()
                .setNotification(
                    Notification.builder()
                        .setTitle(message.title)
                        .setBody(message.body)
                        .build()
                )
                .setToken(fcmToken)
                .build()

            val firebaseMessaging = FirebaseMessaging.getInstance(firebaseApp)
            val response = firebaseMessaging.send(fcmMessage)
            logger.info("Successfully sent FCM notification: $response - $message")
        } catch (e: Exception) {
            logger.error("Failed to send FCM notification: ${e.message}", e)
        }
    }
}

data class NotificationMessage(
    val title: String,
    val body: String
)