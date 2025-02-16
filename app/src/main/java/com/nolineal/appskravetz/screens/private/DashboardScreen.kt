package com.nolineal.appskravetz.screens.private

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nolineal.appskravetz.navigation.Routes
import com.nolineal.appskravetz.viewmodel.SharedAuthViewModel

@Composable
fun DashboardScreen(
    navigation: NavHostController,
    authViewModel: SharedAuthViewModel
) {
    val currentUser = authViewModel.authState.observeAsState().value?.currentUser?.userData
    val authState = authViewModel.authState.observeAsState().value

    LaunchedEffect(Unit) {
        Log.d("DashboardScreen", "!!")
        Log.d("DashboardScreen, Logged User?:", authState?.loggedUser.toString())
    }

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
        if (currentUser == null) {
            Log.e("DashboardScreen", "No user data found")
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