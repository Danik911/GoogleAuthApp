package com.example.googleauthapp.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ApiResponse(
    val message: String? = null,
    val success: Boolean,
    val user: User? = null,

    @Transient
    val error: Exception? = null
)
