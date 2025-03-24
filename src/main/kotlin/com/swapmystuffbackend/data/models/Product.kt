package com.swapmystuffbackend.data.models

import jakarta.persistence.*

@Entity
@Table(name = "products")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    val id: Long? = null, // Changed to val
    @Column(name = "name", nullable = false)
    val name: String,
    @Column(name = "description", nullable = false)
    val description: String,
    @Column(name = "category")
    val category: String? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: ProductStatus, // Changed to val
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "image_url")
    val imageUrls: List<String> = emptyList(),
    @ElementCollection
    @CollectionTable(name = "product_documents", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "document_url")
    val documentUrls: List<String> = emptyList(),
    @ElementCollection
    @CollectionTable(name = "product_videos", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "video_url")
    val videoUrls: List<String> = emptyList(),
    @Column(name = "is_favorite")
    val isFavorite: Boolean = false, // Changed to val
    @Column(name = "favorite_count")
    val favoriteCount: Int = 0, // Changed to val
    @Column(name = "original_product1_id")
    val originalProduct1Id: String? = null,
    @Column(name = "original_product2_id")
    val originalProduct2Id: String? = null,
    @Column(name = "combined_name")
    val combinedName: String? = null,
    @Column(name = "combined_description")
    val combinedDescription: String? = null,
    @Column(name = "combined_category")
    val combinedCategory: String? = null,
    @ElementCollection
    @CollectionTable(name = "combined_images", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "combined_image_url")
    val combinedImageUrls: List<String> = emptyList(),
    @ElementCollection
    @CollectionTable(name = "combined_documents", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "combined_document_url")
    val combinedDocumentUrls: List<String> = emptyList(),
    @ElementCollection
    @CollectionTable(name = "combined_videos", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "combined_video_url")
    val combinedVideoUrls: List<String> = emptyList(),
    @Column(name = "original_owner_id")
    val originalOwnerId: String? = null,
    @Column(name = "original_product_id")
    val originalProductId: Int? = null,
    @Column(name = "owner_id")
    val ownerId: String
)