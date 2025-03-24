package com.swapmystuffbackend.data.repositories

import com.swapmystuffbackend.data.models.SwapRequest
import com.swapmystuffbackend.data.models.SwapRequestStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SwapRequestRepository : JpaRepository<SwapRequest, Long> {
    fun findByUserId(userId: String): List<SwapRequest>
    fun findByOwnerId(ownerId: String): List<SwapRequest>
    fun findByStatus(status: SwapRequestStatus): List<SwapRequest>
}