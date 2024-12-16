package com.bacancy.samplebaseapp.forKoin

import com.bacancy.samplebaseapp.silentUpdates.ApiService
import com.bacancy.samplebaseapp.silentUpdates.UsersResponse

class UserRepositoryImpl(private val api: ApiService) : UserRepository {
    override suspend fun getUsers(): UsersResponse {
        return api.getAllUsers()
    }
}