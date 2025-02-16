package com.nolineal.appskravetz.viewmodel

import com.nolineal.appskravetz.domain.User

data class SharedCurrentUser(
    val currentUserEmail: String? = null,
    val userData: User? = null
)

data class SharedAuthState(
    var currentUser: SharedCurrentUser = SharedCurrentUser(),
    val loggedUser: Boolean = false,
)