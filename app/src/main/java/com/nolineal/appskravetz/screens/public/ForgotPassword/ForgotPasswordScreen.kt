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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nolineal.appskravetz.navigation.Routes
import com.nolineal.appskravetz.viewmodel.SharedAuthViewModel

enum class ForgotPasswordFormState(value: String) {
    INITIAL("INITIAL"),
    NO_EMAIL_GIVEN("NO_EMAIL_GIVEN"),
    ERROR("ERROR"),
    SUCCESS("SUCCESS")
}


@Composable
fun ForgotPasswordScreen(
    navigation: NavHostController,
    authViewModel: SharedAuthViewModel
) {
    val authstate by authViewModel.authState.observeAsState()
    var userEmail by remember { mutableStateOf("") }
    var formState by remember { mutableStateOf(ForgotPasswordFormState.INITIAL) }


    fun onSubmitHandler() {

        if (formState == ForgotPasswordFormState.INITIAL) {
            Log.i("onSubmitHandler", "Iniciando envío de correo...")
            val cleanedEmail = userEmail.trim().lowercase()
            authViewModel.updateUserPassword(cleanedEmail)
            formState = ForgotPasswordFormState.SUCCESS
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

        if (formState == ForgotPasswordFormState.INITIAL || formState == ForgotPasswordFormState.NO_EMAIL_GIVEN) {
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


        Spacer(modifier = Modifier.height(16.dp))

        if (formState == ForgotPasswordFormState.NO_EMAIL_GIVEN
        ) {
            Text(
                text = "Por favor, ingresa un e-mail",
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
                Text(text = "Enviar correo para reestablecer contraseña")
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