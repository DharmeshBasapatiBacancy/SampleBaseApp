package com.bacancy.samplebaseapp.forKoin

import com.bacancy.samplebaseapp.silentUpdates.UsersResponse

interface UserRepository {
    suspend fun getUsers(): UsersResponse
}