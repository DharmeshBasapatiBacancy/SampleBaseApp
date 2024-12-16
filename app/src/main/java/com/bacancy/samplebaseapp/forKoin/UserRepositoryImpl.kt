package com.bacancy.samplebaseapp.forKoin

import androidx.lifecycle.LiveData
import com.bacancy.samplebaseapp.local.TbUser
import com.bacancy.samplebaseapp.local.UserDao
import com.bacancy.samplebaseapp.silentUpdates.ApiService
import com.bacancy.samplebaseapp.silentUpdates.UsersResponse

class UserRepositoryImpl(private val api: ApiService, private val userDao: UserDao) : UserRepository {
    override suspend fun getUsers(): UsersResponse {
        return api.getAllUsers()
    }

    override suspend fun insertAllUsers(users: List<TbUser>) = userDao.insertAll(users)

    override fun getUsersFromDB(): LiveData<List<TbUser>> {
        return userDao.getAllUsers()
    }
}