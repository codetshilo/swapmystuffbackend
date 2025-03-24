package com.swapmystuffbackend.services

import com.swapmystuffbackend.data.models.User
import com.swapmystuffbackend.data.repositories.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    @Transactional
    fun createUser(user: User): User {
        return userRepository.save(user)
    }

    fun getUserById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun getUserByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    @Transactional
    fun updateUser(id: Long, updatedUser: User): User? {
        return userRepository.findById(id).map {
            userRepository.save(updatedUser.copy(id = it.id))
        }.orElse(null)
    }

    @Transactional
    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }

    fun getFcmTokenForUser(userId: String): String? {
        return userRepository.findByEmail(userId)?.fcmToken
    }

    @Transactional
    fun updateFcmToken(userId: String, fcmToken: String): User? {
        return userRepository.findByEmail(userId)?.let {
            userRepository.save(it.copy(fcmToken = fcmToken))
        }
    }
}