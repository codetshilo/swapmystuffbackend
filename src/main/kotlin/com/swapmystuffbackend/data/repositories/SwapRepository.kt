package com.swapmystuffbackend.data.repositories

import com.swapmystuffbackend.data.models.SwapData
import com.swapmystuffbackend.data.models.SwapStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SwapRepository : JpaRepository<SwapData, String> {
    fun findByStatus(status: SwapStatus): List<SwapData>
    fun findByUserId(userId: String): List<SwapData>
}