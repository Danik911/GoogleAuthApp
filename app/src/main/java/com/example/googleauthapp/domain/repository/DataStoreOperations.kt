package com.example.googleauthapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreOperations {

    suspend fun insertLoginState(loginState: Boolean)
    fun readLoginState(): Flow<Boolean>
}