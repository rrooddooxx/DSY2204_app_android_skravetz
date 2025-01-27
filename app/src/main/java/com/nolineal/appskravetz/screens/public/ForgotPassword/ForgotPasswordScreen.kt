package com.nolineal.appskravetz.screens.public.ForgotPassword

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.nolineal.appskravetz.domain.User
import com.nolineal.appskravetz.navigation.Routes
import com.nolineal.appskravetz.viewmodel.SharedAuthViewModel

enum class ForgotPasswordFormState(value: String) {
    INITIAL("INITIAL"),
    NO_EMAIL_GIVEN("NO_EMAIL_GIVEN"),
    FOUND_USER("FOUND_USER"),
    USER_NOT_FOUND("USER_NOT_FOUND"),
    ERROR("ERROR"),
    SUCCESS("SUCCESS")
}


@Composable
fun ForgotPasswordScreen(
    navigation: NavHostController,
    authViewModel: SharedAuthViewModel
) {
    var userEmail by remember { mutableStateOf("") }
    var foundUser by remember { mutableStateOf<User?>(null) }
    var formState by remember { mutableStateOf(ForgotPasswordFormState.INITIAL) }
    val usersState =
        authViewModel.userState.collectAsStateWithLifecycle().value
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordMatchError by remember { mutableStateOf(false) }

    fun setNewPassword(pass: String, confirm: Boolean) {

        if (newPassword == confirmPassword) {
            passwordMatchError = false
        }

        if (confirm) {
            confirmPassword = pass
        } else {
            newPassword = pass
        }
    }


    fun findUserInRegisteredUsers(email: String): User? {
        return usersState.registeredUsers.find { user ->
            user.email == email
        }
    }

    fun onSubmitHandler() {

        if (formState == ForgotPasswordFormState.INITIAL) {
            Log.i("onSubmitHandler", "Checkeando usuario...")
            val cleanedEmail = userEmail.trim().lowercase()
            val foundUserInList = findUserInRegisteredUsers(cleanedEmail)
            foundUserInList?.let { user ->
                Log.i("foundUser", user.toString())
                foundUser = user
                formState = ForgotPasswordFormState.FOUND_USER
                return
            }

            formState = ForgotPasswordFormState.USER_NOT_FOUND
        }

        if (formState == ForgotPasswordFormState.FOUND_USER) {
            Log.i("onSubmitHandler", "Usuario encontrado... $foundUser")

            if (newPassword != confirmPassword || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                passwordMatchError = true
                return
            }

            if (foundUser == null) {
                formState = ForgotPasswordFormState.ERROR
                return
            }

            val wasChangedOk =
                foundUser?.let { authViewModel.updateUserPassword(it, newPassword) }

            if (wasChangedOk == true) {
                formState = ForgotPasswordFormState.SUCCESS
            }

        }


    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = generateHeadingText(formState),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (formState == ForgotPasswordFormState.INITIAL || formState == ForgotPasswordFormState.USER_NOT_FOUND || formState == ForgotPasswordFormState.NO_EMAIL_GIVEN) {
            OutlinedTextField(
                value = userEmail,
                onValueChange = {
                    formState = ForgotPasswordFormState.INITIAL
                    userEmail = it
                },
                label = { Text("Ingresa el e-mail aquí") },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (formState == ForgotPasswordFormState.FOUND_USER) {
            ForgotPasswordChangeComponent(
                setNewPassword = { pass, confirm ->
                    setNewPassword(pass, confirm)
                },
                newPassword,
                confirmPassword,
                passwordMatchError
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (formState == ForgotPasswordFormState.USER_NOT_FOUND
            || formState == ForgotPasswordFormState.NO_EMAIL_GIVEN
        ) {
            Text(
                text = if (formState == ForgotPasswordFormState.USER_NOT_FOUND) "Usuario no encontrado! Revisa el e-mail" else "Por favor, ingresa un e-mail",
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }


        if (formState != ForgotPasswordFormState.SUCCESS) {
            Button(onClick = {
                if (userEmail.isEmpty()) {
                    formState = ForgotPasswordFormState.NO_EMAIL_GIVEN
                }

                if (userEmail.isNotEmpty()) {
                    if (formState == ForgotPasswordFormState.NO_EMAIL_GIVEN) {
                        Log.d("onCLick", "not empty")
                        formState = ForgotPasswordFormState.INITIAL
                    }
                    onSubmitHandler()
                }

            }) {
                Text(text = if (formState == ForgotPasswordFormState.INITIAL) "Buscar Usuario" else "Cambiar Contraseña")
            }
        }

        if (formState == ForgotPasswordFormState.SUCCESS) {
            Button(onClick = {
                navigation.navigate(Routes.LoginScreen)
            }) {
                Text(text = "Ir a realizar login")
            }
        }

    }
}