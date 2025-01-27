package com.nolineal.appskravetz.viewmodel

import com.nolineal.appskravetz.domain.User

data class SharedCurrentUser(
    val isLogged: Boolean = false,
    val userData: User? = null
)

data class SharedAuthState(
    var registeredUsers: List<User> = mutableListOf(),
    var currentUser: SharedCurrentUser = SharedCurrentUser()
)