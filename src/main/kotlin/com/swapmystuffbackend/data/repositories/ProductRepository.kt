package com.swapmystuffbackend.data.repositories

import com.swapmystuffbackend.data.models.Product
import com.swapmystuffbackend.data.models.ProductStatus
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> { // Changed String to Long
    fun findByNameContainingIgnoreCase(name: String): List<Product>
    fun findByStatus(status: ProductStatus): List<Product>
    fun findByOwnerId(ownerId: String): List<Product>
}