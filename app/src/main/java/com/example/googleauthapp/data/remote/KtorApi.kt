package com.example.googleauthapp.data.remote

import com.example.googleauthapp.domain.model.ApiRequest
import com.example.googleauthapp.domain.model.ApiResponse
import com.example.googleauthapp.domain.model.UserUpdate
import retrofit2.http.*

interface KtorApi {

    @POST("/token_verification")
    suspend fun tokenVerification(
        @Body apiRequest: ApiRequest
    ): ApiResponse

    @DELETE("/delete_user")
    suspend fun deleteUser(): ApiResponse

    @PUT("/update_user_info")
    suspend fun updateUser(
        @Body userUpdate: UserUpdate
    ): ApiResponse

    @GET("/sign_out")
    suspend fun singOut(): ApiResponse

    @GET("/get_user_info")
    suspend fun getUser(): ApiResponse

}