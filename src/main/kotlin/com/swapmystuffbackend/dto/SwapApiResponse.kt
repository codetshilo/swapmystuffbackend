package com.swapmystuffbackend.dto

data class SwapApiResponse(
    val id: String,
    val data: Any?,
    val success: Boolean,
    val message: String
)