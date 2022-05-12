package com.example.googleauthapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: String,
    val name: String,
    val email: String,
    val profileImage: String
)
