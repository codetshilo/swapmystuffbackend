package com.swapmystuffbackend.services // Fixed package

import com.swapmystuffbackend.data.models.SwapRequest
import com.swapmystuffbackend.data.models.SwapRequestStatus
import com.swapmystuffbackend.data.repositories.SwapRequestRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SwapRequestService(private val swapRequestRepository: SwapRequestRepository) {

    @Transactional
    fun createSwapRequest(swapRequest: SwapRequest): SwapRequest {
        return swapRequestRepository.save(swapRequest)
    }

    fun getSwapRequestById(id: Long): SwapRequest? {
        return swapRequestRepository.findById(id).orElse(null)
    }

    @Transactional
    fun updateSwapRequestStatus(id: Long, newStatus: SwapRequestStatus): SwapRequest? {
        return swapRequestRepository.findById(id).map { request ->
            swapRequestRepository.save(request.copy(status = newStatus))
        }.orElse(null)
    }

    fun initiateSwapRequest(swapRequest: SwapRequest): SwapRequest? {
        // Validate users/products exist
        return swapRequestRepository.save(swapRequest)
    }

    fun getSwapRequestsByUserId(userId: String): List<SwapRequest> {
        return swapRequestRepository.findByUserId(userId)
    }

    fun getSwapRequestsByOwnerId(ownerId: String): List<SwapRequest> {
        return swapRequestRepository.findByOwnerId(ownerId)
    }

    fun getSwapRequestsByStatus(status: SwapRequestStatus): List<SwapRequest> {
        return swapRequestRepository.findByStatus(status)
    }

    @Transactional
    fun deleteSwapRequest(id: Long) {
        swapRequestRepository.deleteById(id)
    }
}