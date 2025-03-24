package com.swapmystuffbackend.services

import com.swapmystuffbackend.data.models.*
import com.swapmystuffbackend.data.repositories.SwapRepository
import com.swapmystuffbackend.data.repositories.MessageRepository
import com.swapmystuffbackend.notifications.NotificationService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class SwapService(
    private val swapRepository: SwapRepository,
    private val messageRepository: MessageRepository,
    private val notificationService: NotificationService,
    @Lazy private val userService: UserService // Inject with @Lazy to avoid circular dependency
) {
    fun getAllSwaps(): List<SwapData> = swapRepository.findAll()

    fun getSwapsByUser(userId: String): List<SwapData> = swapRepository.findByUserId(userId)

    @Transactional
    fun createSwap(swapData: SwapData): SwapData {
        val savedSwap = swapRepository.save(swapData)
        notificationService.sendSwapNotification(
            "Product 1", // Replace with actual product names if ProductService is integrated
            "Product 2",
            savedSwap.userId,
            savedSwap.swapWithUserId
        )
        return savedSwap
    }

    @Transactional
    fun createSwapFromRequest(request: SwapRequest): SwapData {
        val swapData = SwapData(
            id = UUID.randomUUID().toString(),
            userId = request.userId,
            swapWithUserId = request.swapWithUserId,
            productIds = listOf(request.selectedProductId.toString(), request.swapWithProductId.toString()),
            status = SwapStatus.PENDING,
            isFavorite = false,
            favoriteCount = 0
        )
        val savedSwap = swapRepository.save(swapData)
        notificationService.sendSwapNotification(
            "Product ${request.selectedProductId}", // Fetch real names via ProductService if needed
            "Product ${request.swapWithProductId}",
            savedSwap.userId,
            savedSwap.swapWithUserId
        )
        return savedSwap
    }

    fun getSwapById(id: String): SwapData? = swapRepository.findById(id).orElse(null)

    @Transactional
    fun acceptSwap(swapId: String): Boolean {
        val swap = swapRepository.findById(swapId).orElse(null) ?: return false
        if (swap.status != SwapStatus.PENDING) return false
        swap.status = SwapStatus.ACCEPTED
        swapRepository.save(swap)
        notificationService.sendNotification(
            userService.getFcmTokenForUser(swap.userId) ?: return true,
            "Swap Accepted",
            "Your swap $swapId has been accepted."
        )
        notificationService.sendNotification(
            userService.getFcmTokenForUser(swap.swapWithUserId) ?: return true,
            "Swap Accepted",
            "Your swap $swapId has been accepted."
        )
        return true
    }

    @Transactional
    fun acceptProductInSwap(swapId: String): Boolean {
        val swap = swapRepository.findById(swapId).orElse(null) ?: return false
        swapRepository.save(swap) // Add specific logic if needed
        return true
    }

    fun getPendingSwaps(): List<SwapData> = swapRepository.findByStatus(SwapStatus.PENDING)
    fun getCompletedSwaps(): List<SwapData> = swapRepository.findByStatus(SwapStatus.COMPLETED)

    @Transactional
    fun updateSwapStatus(id: String, status: String): Boolean {
        val swap = swapRepository.findById(id).orElse(null) ?: return false
        swap.status = SwapStatus.valueOf(status.uppercase())
        swapRepository.save(swap)
        notificationService.sendNotification(
            userService.getFcmTokenForUser(swap.userId) ?: return true,
            "Swap Status Updated",
            "Swap $id status changed to $status"
        )
        notificationService.sendNotification(
            userService.getFcmTokenForUser(swap.swapWithUserId) ?: return true,
            "Swap Status Updated",
            "Swap $id status changed to $status"
        )
        return true
    }

    fun getMessagesForSwap(swapId: String): List<Message> = messageRepository.findBySwapId(swapId)

    @Transactional
    fun sendMessage(swapId: String, message: Message): Message? {
        swapRepository.findById(swapId).orElse(null) ?: return null
        val savedMessage = messageRepository.save(message.copy(swapId = swapId))
        notificationService.sendMessageNotification(
            swapId,
            message.senderId,
            message.receiverId,
            message.messageText
        )
        return savedMessage
    }
}