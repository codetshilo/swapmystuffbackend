package com.swapmystuffbackend.notifications

import org.springframework.stereotype.Component

@Component
class NotificationHelper {
    fun prepareWelcomeNotification(): NotificationMessage {
        return NotificationMessage(
            title = "Welcome",
            body = "Thanks for joining SwapMyStuff!"
        )
    }
}