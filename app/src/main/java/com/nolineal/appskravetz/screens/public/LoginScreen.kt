package com.nolineal.appskravetz.screens.public

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nolineal.appskravetz.domain.LoginFormStates
import com.nolineal.appskravetz.navigation.Routes
import com.nolineal.appskravetz.ui.AppLogo
import com.nolineal.appskravetz.viewmodel.SharedAuthViewModel


@Composable
fun LoginScreen(
    navigation: NavHostController,
    authViewModel: SharedAuthViewModel
) {
    var userEmail by rememberSaveable { mutableStateOf("") }
    val formState by authViewModel.loginFormState.observeAsState()
    var password by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(formState) {
        if (formState == LoginFormStates.SUCCESS) {
            navigation.navigate(route = Routes.DashboardScreen)
            authViewModel.setLoadingState(false)
        }
    }

    fun navigateToRegister() {
        navigation.navigate(route = Routes.RegisterScreen)
    }

    fun navigateToForgotPassword() {
        navigation.navigate(route = Routes.ForgotPasswordScreen)
    }

    fun doLogIn() {
        try {
            if (userEmail.isEmpty() || password.isEmpty()) {
                authViewModel.setLoginFormState(LoginFormStates.EMPTY)
                return
            }

            authViewModel.login(userEmail, password)
            Log.i("logIn", "Login successful for user $userEmail")
        } catch (e: Exception) {
            Log.e("LoginScreen", "Error: ${e.message}")
            authViewModel.setLoginFormState(LoginFormStates.INVALID)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AppLogo(
            modifier = Modifier.padding(vertical = 24.dp)
        )

        OutlinedTextField(
            value = userEmail,
            onValueChange = { value ->
                run {
                    if (value.isNotEmpty()) {
                        authViewModel.setLoginFormState(LoginFormStates.INITIAL)
                        userEmail = value
                    }
                    userEmail = value
                }
            },
            label = { Text("E-Mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { value ->
                run {
                    if (value.isNotEmpty()) {
                        authViewModel.setLoginFormState(LoginFormStates.INITIAL)
                        password = value
                    }
                    password = value
                }
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (formState == LoginFormStates.INVALID || formState == LoginFormStates.EMPTY || formState == LoginFormStates.ERROR) {
            val msg =
                if (formState == LoginFormStates.INVALID || formState == LoginFormStates.ERROR) "Lo sentimos, tus credenciales son inválidas. Intenta nuevamente." else "Por favor, ingresa tus datos"
            Text(
                text = msg,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error,

                )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
            doLogIn()
        }) {
            Text("Iniciar Sesión")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            TextButton(
                onClick = { navigateToRegister() },
                modifier = Modifier.padding(all = 0.dp)
            ) {
                Text(
                    "Registrar Nuevo Usuario",
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.padding(all = 0.dp)
                )
            }
            TextButton(
                onClick = { navigateToForgotPassword() },
                modifier = Modifier.padding(all = 0.dp)
            ) {
                Text(
                    "Olvidé mi contraseña",
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.padding(all = 0.dp)
                )
            }

        }
    }
}