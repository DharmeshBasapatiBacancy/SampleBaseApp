package com.bacancy.samplebaseapp.forKoin

interface UserRepository {
    suspend fun getUsers(): List<User>
}