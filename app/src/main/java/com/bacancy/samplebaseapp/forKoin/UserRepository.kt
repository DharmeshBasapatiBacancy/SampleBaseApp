package com.bacancy.samplebaseapp.forKoin

import androidx.lifecycle.LiveData
import com.bacancy.samplebaseapp.local.TbUser
import com.bacancy.samplebaseapp.silentUpdates.UsersResponse

interface UserRepository {
    suspend fun getUsers(): UsersResponse

    suspend fun insertAllUsers(users: List<TbUser>)

    fun getUsersFromDB(): LiveData<List<TbUser>>
}