package com.nolineal.appskravetz.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nolineal.appskravetz.data.AppData
import com.nolineal.appskravetz.navigation.Routes

@Composable
fun DashboardScreen(
    navigation: NavHostController,
    appData: AppData
) {
    val currentUser = appData.getCurrentUser()

    fun logOutUser() {
        navigation.navigate(route = Routes.LoginScreen)
        return appData.removeCurrentUser()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (currentUser === null) {
            return
        }

        Text(
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            text = "¡Bienvenido, ${currentUser.firstName} ${currentUser.lastNameFather}! \n" +
                    "Tu correo es ${currentUser.email}"
        )
        Spacer(modifier = Modifier.padding(16.dp))

        Button(
            onClick = { logOutUser() }
        ) {
            Text("Cerrar Sesión")
        }
    }
}