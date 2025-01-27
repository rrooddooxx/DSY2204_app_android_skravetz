package com.nolineal.appskravetz.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nolineal.appskravetz.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_data")

class AppDataStore
    (private val context: Context) {

    companion object {
        val CURRENT_USER = stringPreferencesKey("current_user")
        val REGISTERED_USERS = stringPreferencesKey("registered_users")
    }

    suspend fun saveUsers(users: Array<User>) {
        val jsonDataString = Json.encodeToString(users)
        context.dataStore.edit { preferences ->
            preferences[REGISTERED_USERS] = jsonDataString
        }
    }

    suspend fun setCurrentUser(user: User) {
        val jsonDataString = Json.encodeToString(user)
        context.dataStore.edit { preferences ->
            preferences[CURRENT_USER] = jsonDataString
        }
    }

    fun getUsers(): Flow<Array<User>> {
        return context.dataStore.data.map { preferences ->
            val userJsonString = preferences[REGISTERED_USERS] ?: "[]"
            Json.decodeFromString<Array<User>>(userJsonString)
        }
    }

    fun getCurrentUser(): Flow<User> {
        return context.dataStore.data.map { preferences ->
            val userJsonString = preferences[CURRENT_USER] ?: "[]"
            Json.decodeFromString<User>(userJsonString)
        }
    }
}