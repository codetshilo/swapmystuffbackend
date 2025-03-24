package com.swapmystuffbackend.services

import com.swapmystuffbackend.data.models.Product
import com.swapmystuffbackend.data.models.ProductStatus
import com.swapmystuffbackend.data.repositories.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(private val productRepository: ProductRepository) {

    @Transactional
    fun createProduct(product: Product): Product {
        return productRepository.save(product)
    }

    fun getAllProducts(): List<Product> { // Added Product type
        return productRepository.findAll()
    }

    fun getProductById(id: Long): Product? {
        return productRepository.findById(id).orElse(null)
    }

    @Transactional
    fun updateProduct(id: Long, updatedProduct: Product): Product? {
        return productRepository.findById(id).map {
            productRepository.save(updatedProduct.copy(id = it.id))
        }.orElse(null)
    }

    @Transactional
    fun deleteProduct(id: Long) {
        productRepository.deleteById(id)
    }

    fun getProductsByOwnerId(ownerId: String): List<Product> { // Added Product type
        return productRepository.findByOwnerId(ownerId)
    }

    fun getProductsByStatus(status: ProductStatus): List<Product> { // Added Product type
        return productRepository.findByStatus(status)
    }

    fun searchProductsByName(name: String): List<Product> { // Added Product type
        return productRepository.findByNameContainingIgnoreCase(name)
    }

    @Transactional
    fun updateFavoriteStatus(id: Long, favorite: Boolean): Product? {
        return productRepository.findById(id).map {
            val newCount = if (favorite) it.favoriteCount + 1 else maxOf(0, it.favoriteCount - 1)
            productRepository.save(it.copy(isFavorite = favorite, favoriteCount = newCount))
        }.orElse(null)
    }
}