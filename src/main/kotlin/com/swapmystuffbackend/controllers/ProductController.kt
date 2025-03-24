package com.swapmystuffbackend.controllers

import com.swapmystuffbackend.data.models.Product
import com.swapmystuffbackend.data.models.ProductStatus
import com.swapmystuffbackend.data.repositories.ProductRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class ProductController(private val productRepository: ProductRepository) {

    @GetMapping("/search")
    fun searchProductsByName(@RequestParam name: String): ResponseEntity<List<Product>> {
        val products = productRepository.findByNameContainingIgnoreCase(name)
        return ResponseEntity.ok(products)
    }

    @GetMapping("/searchById")
    fun searchBySwapId(@RequestParam swapId: String): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(emptyList()) // Placeholder
    }

    @PostMapping("/upload")
    fun uploadProduct(@RequestBody product: Product): ResponseEntity<String> {
        if (product.name.isBlank() || product.description.isBlank()) {
            return ResponseEntity.badRequest().body("Name and description are required")
        }
        val savedProduct = productRepository.save(product.copy(id = null))
        return ResponseEntity.ok(savedProduct.id.toString())
    }

    @PostMapping("/create")
    fun createProduct(@RequestBody product: Product): ResponseEntity<String> {
        if (product.name.isBlank() || product.description.isBlank()) {
            return ResponseEntity.badRequest().body("Name and description are required")
        }
        val savedProduct = productRepository.save(product.copy(id = null))
        return ResponseEntity.ok(savedProduct.id.toString())
    }

    @GetMapping
    fun getProducts(@RequestParam status: ProductStatus): ResponseEntity<List<Product>> {
        val products = productRepository.findByStatus(status)
        return ResponseEntity.ok(products)
    }

    @GetMapping("/user")
    fun getProductsByUser(@RequestParam userId: String): ResponseEntity<List<Product>> {
        val products = productRepository.findByOwnerId(userId)
        return ResponseEntity.ok(products)
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: String): ResponseEntity<Unit> {
    productRepository.deleteById(id.toLong()) // Convert to Long for repository
    return ResponseEntity.ok().build()
    }

    @PostMapping("/{productId}/favorite")
    fun toggleFavoriteStatus(@PathVariable productId: String): ResponseEntity<Product> {
    val product = productRepository.findById(productId.toLong()).orElseThrow { IllegalArgumentException("Product not found") }
    return ResponseEntity.ok(product)
    }

    @PostMapping("/{productId}/unfavorite")
    fun unfavoriteProduct(@PathVariable productId: Long): ResponseEntity<Product> {
        val product = productRepository.findById(productId).orElseThrow { IllegalArgumentException("Product not found") }
        return ResponseEntity.ok(product)
    }

    @GetMapping("/{productId}/favoriteCount")
    fun getFavoriteCount(@PathVariable productId: Long): ResponseEntity<Int> {
        return ResponseEntity.ok(0) // Placeholder
    }

    @PostMapping("/{productId}/updateFavoriteCount")
    fun updateFavoriteCount(@PathVariable productId: Long, @RequestParam newCount: Int): ResponseEntity<Unit> {
        return ResponseEntity.ok().build() // Placeholder
    }

    @PostMapping("/{productId}/updateStatus")
    fun updateProductStatus(
        @PathVariable productId: Long,
        @RequestBody product: Product
    ): ResponseEntity<Product> {
        val existingProduct = productRepository.findById(product.id ?: productId).orElseThrow { IllegalArgumentException("Product not found") }
        val updatedProduct = existingProduct.copy(status = product.status)
        productRepository.save(updatedProduct)
        return ResponseEntity.ok(updatedProduct)
    }
}