package com.example.googleauthapp.domain.model

import java.lang.Exception

data class MessageBarState(
    val message: String? = null,
    val error: Exception? = null
)
