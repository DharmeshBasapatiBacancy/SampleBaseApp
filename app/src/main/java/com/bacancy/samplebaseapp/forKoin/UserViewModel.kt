package com.bacancy.samplebaseapp.forKoin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bacancy.samplebaseapp.silentUpdates.UsersResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _users = MutableStateFlow(UsersResponse())
    val users: StateFlow<UsersResponse> = _users.asStateFlow()
    val usersFromDB = userRepository.getUsersFromDB()

    fun fetchUsers() {
        viewModelScope.launch {
            _users.value = userRepository.getUsers()
        }
    }

}