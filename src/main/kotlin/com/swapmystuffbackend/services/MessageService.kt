package com.swapmystuffbackend.services

import com.swapmystuffbackend.data.models.Message
import com.swapmystuffbackend.data.repositories.MessageRepository
import com.swapmystuffbackend.notifications.NotificationService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class MessageService(
    private val messageRepository: MessageRepository,
    private val notificationService: NotificationService // Add this
) {

    @Transactional
    fun saveMessage(message: Message): Message {
        val savedMessage = messageRepository.save(message.copy(timestamp = Instant.now().toEpochMilli()))
        notificationService.sendMessageNotification(
            savedMessage.swapId,
            savedMessage.senderId,
            savedMessage.receiverId,
            savedMessage.messageText
        )
        return savedMessage
    }

    fun getMessagesBetweenUsers(userId1: String, userId2: String): List<Message> {
        return messageRepository.findMessagesBetweenUsers(userId1, userId2)
            .sortedBy { it.timestamp }
    }

    fun getMessageById(messageId: Long): Message? {
        return messageRepository.findById(messageId).orElse(null)
    }

    @Transactional
    fun deleteMessage(messageId: Long) {
        messageRepository.deleteById(messageId)
    }

    @Transactional
    fun updateMessageText(messageId: Long, newText: String): Message? {
        val message = messageRepository.findById(messageId).orElse(null)
        return message?.let {
            val updatedMessage = it.copy(messageText = newText)
            messageRepository.save(updatedMessage)
        }
    }
}