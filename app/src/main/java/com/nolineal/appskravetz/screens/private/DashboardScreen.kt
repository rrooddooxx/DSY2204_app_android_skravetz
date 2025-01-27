package com.nolineal.appskravetz.screens.private

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.nolineal.appskravetz.navigation.Routes
import com.nolineal.appskravetz.viewmodel.SharedAuthViewModel

@Composable
fun DashboardScreen(
    navigation: NavHostController,
    authViewModel: SharedAuthViewModel
) {
    val currentUser = authViewModel.userState.collectAsStateWithLifecycle().value.currentUser
    val userData = currentUser.userData

    fun logOutUser() {
        navigation.navigate(route = Routes.LoginScreen)
        return authViewModel.logOut()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (currentUser.userData === null) {
            return
        }

        Text(
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            text = "¡Bienvenido, ${userData?.firstName} ${userData?.lastNameFather}! \n" +
                    "Tu correo es ${userData?.email}"
        )
        Spacer(modifier = Modifier.padding(16.dp))

        Button(
            onClick = { logOutUser() }
        ) {
            Text("Cerrar Sesión")
        }
    }
}