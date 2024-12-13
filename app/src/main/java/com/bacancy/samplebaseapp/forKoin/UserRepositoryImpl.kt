package com.bacancy.samplebaseapp.forKoin

class UserRepositoryImpl : UserRepository {
    override suspend fun getUsers(): List<User> {
        return listOf(
            User(1, "John", "14/06/1993"),
            User(2, "Jane", "23/09/1995"),
            User(3, "Bob", "30/03/1999")
        )
    }
}