package com.bacancy.samplebaseapp.silentUpdates

import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getAllUsers(): UsersResponse
}