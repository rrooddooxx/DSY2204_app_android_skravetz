package com.nolineal.appskravetz.screens.public

import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nolineal.appskravetz.domain.User
import com.nolineal.appskravetz.navigation.Routes
import com.nolineal.appskravetz.viewmodel.SharedAuthViewModel

enum class RegisterScreenFormState(value: String) {
    VALID("VALID"),
    INVALID("INVALID"),
    ERROR("ERROR")
}

@Composable
fun RegisterScreen(
    navigation: NavHostController,
    authViewModel: SharedAuthViewModel
) {
    var name by remember { mutableStateOf("") }
    var fatherLastName by remember { mutableStateOf("") }
    var motherLastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var formState by remember { mutableStateOf(RegisterScreenFormState.VALID) }


    fun registerUser() {
        try {
            if (name.isNotEmpty() && fatherLastName.isNotEmpty()
                && motherLastName.isNotEmpty() && email.isNotEmpty()
                && password.isNotEmpty()
            ) {
                formState = RegisterScreenFormState.VALID
                val newUser =
                    User(name, fatherLastName, motherLastName, email, password)
                authViewModel.registerNewUser(newUser)
                authViewModel.setCurrentUser(toCurrentUser = newUser, isLogged = true)
                navigation.navigate(route = Routes.DashboardScreen)
                return
            }

            formState = RegisterScreenFormState.INVALID
            Log.e("registerUser", "No hay datos válidos!")
        } catch (e: Exception) {
            formState = RegisterScreenFormState.ERROR
            Log.e("registerUser", "Error al registrar el usuario, Error: $e")
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                if (name.length < 1) {
                    Text(text = "** Obligatorio. Ingresa tu primer nombre. ")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = fatherLastName,
            onValueChange = { fatherLastName = it },
            label = { Text("Apellido Paterno") },
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                if (fatherLastName.length < 1) {
                    Text(text = "** Obligatorio. Ingresa tu apellido paterno. ")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = motherLastName,
            onValueChange = { motherLastName = it },
            label = { Text("Apellido Materno") },
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                if (motherLastName.length < 1) {
                    Text(text = "** Obligatorio. Ingresa tu apellido materno. ")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                if (email.length < 1) {
                    Text(text = "** Obligatorio. Ingresa tu correo electrónico. ")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                if (password.length < 1) {
                    Text(text = "** Obligatorio. Ingresa una contraseña. ")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (formState == RegisterScreenFormState.INVALID || formState == RegisterScreenFormState.ERROR) {
            Text(
                text = if (formState == RegisterScreenFormState.INVALID) "¡Atención! Debes rellenar todos los campos"
                else "Ha ocurrido un error, inténtalo nuevamente",
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }


        Button(onClick = { registerUser() }, modifier = Modifier.padding(top = 24.dp)) {
            Text("Registrarse")
        }
    }
}