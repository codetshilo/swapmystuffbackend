package com.swapmystuffbackend.data.repositories

import com.swapmystuffbackend.data.models.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    fun findBySenderIdAndReceiverId(senderId: String, receiverId: String): List<Message>

    @Query("SELECT m FROM Message m WHERE (m.senderId = :senderId AND m.receiverId = :receiverId) OR (m.senderId = :receiverId AND m.receiverId = :senderId)")
    fun findMessagesBetweenUsers(senderId: String, receiverId: String): List<Message>

    fun findBySwapId(swapId: String): List<Message>
}