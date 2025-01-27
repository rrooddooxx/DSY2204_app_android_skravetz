package com.nolineal.appskravetz.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nolineal.appskravetz.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SharedAuthViewModel : ViewModel() {
    private val _currentState = MutableStateFlow(SharedAuthState())
    private val initUsers: List<User> = listOf(
        User("Sebastian", "Kravetz", "Lagneto", "sk@home.org", "Pass1234"),
        User("Pedro", "Gonzalez", "Empanadas", "pg@chile.cl", "Chile123"),
        User("Maria", "Rodriguez", "Valparaiso", "mr@chile.cl", "Santiago456"),
        User("Juan", "Perez", "Atacama", "jp@chile.cl", "Desert789"),
        User("Ana", "Lopez", "Concepcion", "al@chile.cl", "Biobio012")
    )

    val userState: StateFlow<SharedAuthState> = _currentState.asStateFlow()

    init {
        _currentState.update { state ->
            state.copy(registeredUsers = state.registeredUsers + initUsers)
        }
        Log.println(Log.INFO, "SharedAuthViewModel", "Initial users loaded!!")
    }

    fun registerNewUser(newUser: User) {
        _currentState.update { state ->
            state.copy(
                registeredUsers = state.registeredUsers + newUser
            )
        }
    }

    fun setCurrentUser(toCurrentUser: User, isLogged: Boolean) {
        _currentState.update { state ->
            state.copy(
                currentUser = SharedCurrentUser(
                    isLogged = isLogged,
                    userData = toCurrentUser
                )
            )
        }
    }

    fun updateUserPassword(foundUser: User, newPass: String): Boolean {

        val found: User? = _currentState.value.registeredUsers.find { user ->
            user.email == foundUser.email
                    && user.password == foundUser.password
        }

        if (found == null) {
            return false
        }

        _currentState.value = _currentState.value.copy(
            registeredUsers = _currentState.value.registeredUsers.map { user ->
                if (user.email == foundUser.email) {
                    user.copy(password = newPass)
                } else {
                    user
                }
            }
        )

        return true
    }

    fun findUserByEmail(email: String): User? {
        return _currentState.value.registeredUsers.find { user ->
            user.email == email
        }
    }

    fun logOut() {
        _currentState.update { state ->
            state.copy(
                currentUser = SharedCurrentUser(
                    isLogged = false,
                    userData = null,
                )
            )
        }
    }


}