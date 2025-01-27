package com.nolineal.appskravetz.screens.public.ForgotPassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun ForgotPasswordChangeComponent(
    setNewPassword: (pass: String, confirm: Boolean) -> Unit,
    newPassword: String,
    confirmPassword: String,
    passwordMatchError: Boolean
) {

    Column {

        OutlinedTextField(
            value = newPassword,
            onValueChange = { setNewPassword(it, false) },
            label = { Text("Nueva Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { setNewPassword(it, true) },
            label = { Text("Confirmar Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordMatchError,
            modifier = Modifier.fillMaxWidth()
        )

        if (passwordMatchError) {
            Text(
                text = "Las contraseñas no coinciden!",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}