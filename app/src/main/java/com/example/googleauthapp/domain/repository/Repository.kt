package com.example.googleauthapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun insertLoginState(loginState: Boolean)
    fun readLoginState(): Flow<Boolean>
}