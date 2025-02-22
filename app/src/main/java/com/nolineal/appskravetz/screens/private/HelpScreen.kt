package com.nolineal.appskravetz.screens.private

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nolineal.appskravetz.navigation.Routes
import com.nolineal.appskravetz.viewmodel.SharedAuthViewModel

@Composable
fun HelpScreen(
    navigation: NavHostController,
    authViewModel: SharedAuthViewModel
) {
    val currentUser = authViewModel.authState.observeAsState().value?.currentUser?.userData
    val authState = authViewModel.authState.observeAsState().value

    LaunchedEffect(Unit) {
        Log.d("DashboardScreen", "!!")
        Log.d("DashboardScreen, Logged User?:", authState?.loggedUser.toString())
    }

    fun navigate(route: String) {
        navigation.navigate(route)
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
            text = "Tus datos de usuario:"
        )
        Text(
            modifier = Modifier.padding(24.dp).background(color = Color.LightGray).border(width = 1.dp, color = Color.Gray).padding(all = 12.dp),
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            text = "Nombre: ${currentUser.firstName}\n" +
                    "Primer Apellido: ${currentUser.lastNameFather}! \n" +
                    "Segundo Apellido: ${currentUser.lastNameMother}! \n" +
                    "E-Mail: \n ${currentUser.email}"
        )
        Spacer(modifier = Modifier.padding(16.dp))
        HorizontalDivider(
            color = Color.LightGray,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        Text(
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            text = "En la pantalla anterior puedes seleccionar la funcionalidad deseada"
        )

        Button(
            onClick = { navigate(Routes.DashboardScreen) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Volver al Dashboard")
        }

        Text(
            modifier = Modifier.padding(16.dp).background(Color.LightGray),
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            text = "La funcionalidad 'escribir' te permitirá escuchar con una voz generativa aquello que escribas en la caja de texto. Esto se conoce como text-to-speech (texto-a-voz)"
        )

        Text(
            modifier = Modifier.padding(16.dp).background(Color.LightGray),
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            text = "La funcionalidad 'hablar' te permitirá habilitar el micrófono, la app escuchará lo que hablas y luego lo transcribirá para que cualquiera pueda leerlo en la pantalla."
        )


    }
}