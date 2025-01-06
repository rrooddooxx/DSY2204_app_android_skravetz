package com.nolineal.appskravetz.data

import com.nolineal.appskravetz.data.entities.User

class AppData {
    val registeredUsers = mutableListOf<User>()
    val currentUser = mutableListOf<User>()

    fun saveUsers(users: List<User>) {
        registeredUsers.addAll(users)
    }

    fun addRegisteredUser(user: User) {
        registeredUsers.add(user)
    }

    fun getUsers(): List<User> {
        return registeredUsers.toList()
    }

    fun findUserByEmail(email: String): User? {
        return registeredUsers.stream().filter { user ->
            user.email == email
        }.findFirst().orElse(null)
    }

    fun setCurrentUser(user: User) {
        currentUser.clear()
        currentUser.add(user)
    }

    fun removeCurrentUser() {
        currentUser.clear()
    }

    fun getCurrentUser(): User? {
        return currentUser.getOrNull(0)
    }
}