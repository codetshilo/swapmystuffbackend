package com.swapmystuffbackend.data.models

import jakarta.persistence.*

@Entity
@Table(name = "swaps")
data class SwapData(
    @Id
    val id: String,
    @Column(name = "user_id")
    val userId: String,
    @Column(name = "swap_with_user_id")
    val swapWithUserId: String,
    @ElementCollection
    val productIds: List<String>,
    @Enumerated(EnumType.STRING)
    var status: SwapStatus,
    var isFavorite: Boolean = false,
    var favoriteCount: Int = 0
)