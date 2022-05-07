package com.example.googleauthapp.data.repository

import com.example.googleauthapp.domain.repository.DataStoreOperations
import com.example.googleauthapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dataStoreOperations: DataStoreOperations
) : Repository {
    override suspend fun insertLoginState(loginState: Boolean) {
        dataStoreOperations.insertLoginState(loginState = loginState)
    }

    override fun readLoginState(): Flow<Boolean> =
        dataStoreOperations.readLoginState()

}