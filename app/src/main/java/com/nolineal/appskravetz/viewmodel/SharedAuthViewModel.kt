package com.nolineal.appskravetz.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nolineal.appskravetz.domain.AuthViewModelException
import com.nolineal.appskravetz.domain.RegisterUserDto
import com.nolineal.appskravetz.domain.User

class SharedAuthViewModel : ViewModel() {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth
    private val _authState = MutableLiveData(SharedAuthState())
    private val currentUser = auth.currentUser
    val authState: LiveData<SharedAuthState?> = _authState

    init {
        if (currentUser != null) {
            getUserDataByUid(currentUser.uid)
        }
    }

    private fun getUserDataByUid(uid: String) {
        Log.d("SharedAuthViewModel", "getUserDataByUid: $uid")
        try {
            firestore.collection(FIREBASE_USER_COLLECTION).document(uid).get()
                .addOnSuccessListener { doc ->
                    Log.d("SharedAuthViewModel", "getUserDataByUid: $doc")
                    if (doc.exists()) {
                        Log.d("firstName", doc["firstName"].toString())
                        _authState.postValue(
                            _authState.value?.copy(
                                currentUser = SharedCurrentUser(
                                    currentUserEmail = currentUser?.email,
                                    userData = User(
                                        firstName = doc["firstName"].toString(),
                                        lastNameFather = doc["lastNameFather"].toString(),
                                        lastNameMother = doc["lastNameMother"].toString(),
                                        email = doc["email"].toString()
                                    ),
                                ),
                                loggedUser = true
                            )
                        )
                    }
                }
        } catch (e: Exception) {
            val errorMsg = "Failed getting user data by uid: ${e.message}"
            Log.e("SharedAuthViewModel", errorMsg)
            throw AuthViewModelException(errorMsg)

        }
    }

    fun registerNewUser(newUser: RegisterUserDto) {
        try {
            Log.d("registerNewUser", newUser.toString())
            auth.createUserWithEmailAndPassword(newUser.email, newUser.password)
                .addOnSuccessListener { doc ->
                    Log.i("SharedAuthViewModel", "Success register new user")
                    registerNewUserData(newUser, doc?.user?.uid!!)

                }
                .addOnFailureListener {
                    val errorMsg = "Failed registering new user"
                    Log.e("SharedAuthViewModel", errorMsg)
                    throw AuthViewModelException(errorMsg)
                }
        } catch (e: Exception) {
            val errorMsg = "Failed registering new user: ${e.message}"
            Log.e("SharedAuthViewModel", errorMsg)
//            throw AuthViewModelException(errorMsg)
        }

    }

    private fun registerNewUserData(newUser: RegisterUserDto, userId: String) {
        try {
            val newUserData = User(
                firstName = newUser.firstName,
                lastNameFather = newUser.lastNameFather,
                lastNameMother = newUser.lastNameMother,
                email = newUser.email
            )

            firestore.collection(FIREBASE_USER_COLLECTION).document(userId)
                .set(
                    newUserData
                )
                .addOnSuccessListener {
                    Log.i("SharedAuthViewModel", "Success registering new user data")
                    getUserDataByUid(auth.currentUser?.uid!!)
                }
                .addOnFailureListener {
                    val errorMsg = "Failed registering new user data"
                    Log.e("SharedAuthViewModel", errorMsg)
                    throw AuthViewModelException(errorMsg)
                }
        } catch (e: Exception) {
            val errorMsg = "Failed registering new user data: ${e.message}"
            Log.e("SharedAuthViewModel", errorMsg)
            throw AuthViewModelException(errorMsg)
        }

    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { user ->
                Log.i("SharedAuthViewModel", "Success login!!")
                Log.i("login, uid:", user?.user?.uid.toString())
                if (user?.user?.uid != null) {
                    getUserDataByUid(user.user?.uid.toString())
                }
            }
            .addOnFailureListener {
                val errorMsg = "Failed login"
                Log.e("SharedAuthViewModel", errorMsg)
                throw AuthViewModelException(errorMsg)
            }
    }


    fun updateUserPassword(email: String) {
        auth.setLanguageCode("es")
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("SharedAuthViewModel", "Email sent.")
                }
            }
            .addOnFailureListener {
                val errorMsg = "Failed sending email"
                Log.e("SharedAuthViewModel", errorMsg)
                throw AuthViewModelException(errorMsg)
            }
    }


    fun logOut() {
        auth.signOut()
        _authState.postValue(
            _authState.value?.copy(
                currentUser = SharedCurrentUser(),
                loggedUser = false
            )
        )
    }
}