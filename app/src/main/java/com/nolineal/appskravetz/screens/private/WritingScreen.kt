package com.nolineal.appskravetz.screens.private

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.nolineal.appskravetz.viewmodel.SharedAuthViewModel

@Composable
fun WritingScreen(
    navigation: NavHostController,
    authViewModel: SharedAuthViewModel
) {
    val currentUser = authViewModel.authState.observeAsState().value?.currentUser?.userData
    val authState = authViewModel.authState.observeAsState().value

    LaunchedEffect(Unit) {
        Log.d("WritingScreen", "...")
        Log.d("WritingScreen, Logged User?:", authState?.loggedUser.toString())
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Writing Screen", color = Color.Red)
    }
}