package com.swapmystuffbackend.dto

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    val swapId: String,
    val otherUserId: String? = null,
    
    @SerializedName("id")
    val id: String,

    @SerializedName("data")
    val data: Any?,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String
)