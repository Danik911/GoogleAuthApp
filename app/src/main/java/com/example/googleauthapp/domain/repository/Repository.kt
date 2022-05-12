package com.example.googleauthapp.domain.repository

import com.example.googleauthapp.domain.model.ApiRequest
import com.example.googleauthapp.domain.model.ApiResponse
import com.example.googleauthapp.domain.model.UserUpdate
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun insertLoginState(loginState: Boolean)
    fun readLoginState(): Flow<Boolean>

    suspend fun tokenVerification(apiRequest: ApiRequest): ApiResponse
    suspend fun deleteUser(): ApiResponse
    suspend fun updateUserInfo(userUpdate: UserUpdate): ApiResponse
    suspend fun signOut(): ApiResponse
    suspend fun getUser(): ApiResponse
}