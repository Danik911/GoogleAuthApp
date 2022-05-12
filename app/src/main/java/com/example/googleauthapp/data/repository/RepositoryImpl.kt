package com.example.googleauthapp.data.repository

import com.example.googleauthapp.data.remote.KtorApi
import com.example.googleauthapp.domain.model.ApiRequest
import com.example.googleauthapp.domain.model.ApiResponse
import com.example.googleauthapp.domain.model.UserUpdate
import com.example.googleauthapp.domain.repository.DataStoreOperations
import com.example.googleauthapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dataStoreOperations: DataStoreOperations,
    private val ktorApi: KtorApi
) : Repository {
    override suspend fun insertLoginState(loginState: Boolean) {
        dataStoreOperations.insertLoginState(loginState = loginState)
    }

    override fun readLoginState(): Flow<Boolean> =
        dataStoreOperations.readLoginState()

    override suspend fun tokenVerification(apiRequest: ApiRequest): ApiResponse {
        return try {
            ktorApi.tokenVerification(apiRequest = apiRequest)
        } catch (e: Exception) {
            ApiResponse(success = false, error = e)
        }

    }

    override suspend fun deleteUser(): ApiResponse {
        return try {
            ktorApi.deleteUser()
        } catch (e: Exception) {
            ApiResponse(success = false, error = e)
        }
    }

    override suspend fun updateUserInfo(userUpdate: UserUpdate): ApiResponse {
        return try {
            ktorApi.updateUser(userUpdate = userUpdate)
        } catch (e: Exception) {
            ApiResponse(success = false, error = e)
        }
    }

    override suspend fun signOut(): ApiResponse {
        return try {
            ktorApi.singOut()
        } catch (e: Exception) {
            ApiResponse(success = false, error = e)
        }
    }

    override suspend fun getUser(): ApiResponse {
        return try {
            ktorApi.getUser()
        } catch (e: Exception) {
            ApiResponse(success = false, error = e)
        }
    }

}