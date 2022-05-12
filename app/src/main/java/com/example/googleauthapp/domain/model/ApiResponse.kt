package com.example.googleauthapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val message: String? = null,
    val success: Boolean,
    val user: User? = null
)
