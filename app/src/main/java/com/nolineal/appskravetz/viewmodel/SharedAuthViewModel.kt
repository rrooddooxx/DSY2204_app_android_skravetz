package com.nolineal.appskravetz.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nolineal.appskravetz.domain.AuthViewModelException
import com.nolineal.appskravetz.domain.LoginFormStates
import com.nolineal.appskravetz.domain.RegisterUserDto
import com.nolineal.appskravetz.domain.User
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SharedAuthViewModel(
    private val firestore: FirebaseFirestore = Firebase.firestore,
    private val auth: FirebaseAuth = Firebase.auth
) : ViewModel() {

    private val _authState = MutableLiveData(SharedAuthState())
    private val currentUser = auth.currentUser
    private val _isLoadingState = MutableLiveData(false)
    private val _isPasswordResetSuccess = MutableLiveData(false)
    private val _loginState = MutableLiveData(LoginFormStates.INITIAL)
    val authState: LiveData<SharedAuthState?> = _authState
    val isLoadingState: LiveData<Boolean> = _isLoadingState
    val isPasswordResetSuccess: LiveData<Boolean> = _isPasswordResetSuccess
    val loginFormState: LiveData<LoginFormStates> = _loginState

    init {
        if (currentUser != null) {
            getUserDataByUid(currentUser.uid)
        }
    }

    fun getUserDataByUid(uid: String) {
        Log.d("SharedAuthViewModel", "getUserDataByUid: $uid")
        try {
            _isLoadingState.postValue(true)
            firestore.collection(FIREBASE_USER_COLLECTION).document(uid).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        _authState.postValue(
                            _authState.value?.copy(
                                currentUser = SharedCurrentUser(
                                    currentUserEmail = currentUser?.email,
                                    userData = User(
                                        firstName = doc["firstName"].toString(),
                                        lastNameFather = doc["lastNameFather"].toString(),
                                        lastNameMother = doc["lastNameMother"].toString(),
                                        email = doc["email"].toString(),
                                        uid = doc.id
                                    ),
                                ),
                                loggedUser = true
                            )
                        )
                        Log.i(
                            "SharedAuthViewModel",
                            "getUserDataByUid: Éxito obteniendo data usuario"
                        )
                    } else throw AuthViewModelException("No existe data usuario uid $uid")
                }
                .addOnCompleteListener {
                    _isLoadingState.postValue(false)
                }
        } catch (e: Exception) {
            val errorMsg = "getUserDataByUid: Error obteniendo data usuario x uid: ${e.message}"
            Log.e("SharedAuthViewModel", errorMsg)
            _isLoadingState.postValue(false)
            throw AuthViewModelException(errorMsg)

        }
    }

    fun registerNewUser(newUser: RegisterUserDto) {
        try {
            Log.d("registerNewUser", newUser.toString())
            Log.d("SharedAuthViewModel", "registerNewUser: Registrando nuevo usuario...")
            _isLoadingState.postValue(true)
            auth.createUserWithEmailAndPassword(newUser.email, newUser.password)
                .addOnSuccessListener { doc ->
                    Log.i(
                        "SharedAuthViewModel",
                        "registerNewUser: Éxito registrando nuevo usuario!"
                    )
                    registerNewUserData(newUser, doc?.user?.uid!!)

                }
                .addOnFailureListener {
                    val errorMsg = "registerNewUser: Error registrando nuevo usuario"
                    Log.e("SharedAuthViewModel", errorMsg)
                    throw AuthViewModelException(errorMsg)
                }
                .addOnCompleteListener {
                    _isLoadingState.postValue(false)
                }
        } catch (e: Exception) {
            val errorMsg = "registerNewUser: Error registrando nuevo usuario: ${e.message}"
            Log.e("SharedAuthViewModel", errorMsg)
            _isLoadingState.postValue(false)
            throw AuthViewModelException(errorMsg)
        }

    }

    private fun registerNewUserData(newUser: RegisterUserDto, userId: String) {
        try {
            val newUserData = User(
                firstName = newUser.firstName,
                lastNameFather = newUser.lastNameFather,
                lastNameMother = newUser.lastNameMother,
                email = newUser.email,
                uid = userId
            )

            firestore.collection(FIREBASE_USER_COLLECTION).document(userId)
                .set(
                    newUserData
                )
                .addOnSuccessListener {
                    Log.i(
                        "SharedAuthViewModel",
                        "registerNewUserData: Éxito registrando data nuevo usuario"
                    )
                    getUserDataByUid(auth.currentUser?.uid.toString())
                }
                .addOnFailureListener {
                    val errorMsg = "registerNewUserData: Error registrando data nuevo usuario"
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
        try {
            _isLoadingState.postValue(true)
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { user ->
                    Log.i("SharedAuthViewModel", "login: Éxito en login!!")
                    if (user?.user?.uid != null) {
                        getUserDataByUid(user.user?.uid.toString())
                        _loginState.postValue(LoginFormStates.SUCCESS)
                    }
                }
                .addOnFailureListener {
                    val errorMsg = "login: Error realizando login"
                    Log.e("SharedAuthViewModel", errorMsg)
                    _loginState.postValue(LoginFormStates.ERROR)
                    _isLoadingState.postValue(false)
                }
        } catch (e: Exception) {
            val errorMsg = "login: Error realizando login!! ${e.message}"
            Log.e("SharedAuthViewModel", errorMsg)
            _loginState.postValue(LoginFormStates.ERROR)
            _isLoadingState.postValue(false)
        }
    }

    fun setLoginFormState(states: LoginFormStates) {
        _loginState.postValue(states)
    }

    fun setLoadingState(state: Boolean) {
        _isLoadingState.postValue(state)
    }

    fun resetUserPassword(email: String) {
        Log.i("updateUserPassword", "Enviando correo...")
        auth.setLanguageCode("es")

        _isLoadingState.postValue(true)
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("SharedAuthViewModel", "updateUserPassword: Correo enviado!")
                    _isPasswordResetSuccess.postValue(true)
                }
            }
            .addOnFailureListener {
                val errorMsg = "updateUserPassword: Error enviando correo de reset password"
                Log.e("SharedAuthViewModel", errorMsg)
                throw AuthViewModelException(errorMsg)
            }
            .addOnCompleteListener {
                _isLoadingState.postValue(false)
            }
    }

    fun resetStatePasswordSuccess() {
        _isPasswordResetSuccess.postValue(false)
    }


    fun logOut() {
        _isLoadingState.postValue(true)
        auth.signOut()
        _authState.postValue(
            _authState.value?.copy(
                currentUser = SharedCurrentUser(),
                loggedUser = false
            )
        )
        setLoginFormState(LoginFormStates.INITIAL)
        _isLoadingState.postValue(false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalUuidApi::class)
    fun registerPrompt(prompt: String, inputMethod: String) {
        val currentInstant = Instant.now()
        val currentZonedDateTime = ZonedDateTime.ofInstant(currentInstant, ZoneId.systemDefault())

        val requestMap = mapOf<String, String>(
            Pair("input_method", inputMethod),
            Pair("prompt", prompt),
            Pair("user_id", _authState.value?.currentUser?.userData?.uid.toString()),
            Pair("generate_at", currentZonedDateTime.toString())
        )

        firestore.collection("requests")
            .document(Uuid.random().toString())
            .set(requestMap)
            .addOnSuccessListener {
                Log.i("SharedAuthViewModel", "registerPrompt: Éxito en registro de prompt")
            }
            .addOnFailureListener {
                Log.e("SharedAuthViewModel", "registerPrompt: Error en registro de prompt")
            }
    }
}