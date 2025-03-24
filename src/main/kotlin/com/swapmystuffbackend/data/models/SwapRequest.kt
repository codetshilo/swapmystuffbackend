package com.swapmystuffbackend.data.models

import jakarta.persistence.*

@Entity
@Table(name = "swap_requests")
data class SwapRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "swap_request_id")
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    val userId: String,

    @Column(name = "owner_id", nullable = false)
    val ownerId: String,

    @Column(name = "swap_with_user_id", nullable = false)
    val swapWithUserId: String,

    @Column(name = "selected_product_id", nullable = false)
    val selectedProductId: Long, // Assuming this should match Product's ID type

    @Column(name = "swap_with_product_id", nullable = false)
    val swapWithProductId: Long, // Assuming this should match Product's ID type

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: SwapRequestStatus = SwapRequestStatus.PENDING
)

enum class SwapRequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}