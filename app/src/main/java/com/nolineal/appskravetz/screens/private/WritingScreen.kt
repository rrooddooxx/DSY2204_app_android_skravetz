package com.nolineal.appskravetz.screens.private

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nolineal.appskravetz.viewmodel.SharedAuthViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WritingScreen(
    navigation: NavHostController,
    authViewModel: SharedAuthViewModel
) {
    val authState = authViewModel.authState.observeAsState().value
    var inputText by remember { mutableStateOf("") }
    var isSpeaking by remember { mutableStateOf(false) }
    val tts = rememberTextToSpeech()

    LaunchedEffect(Unit) {
        Log.d("WritingScreen", "...")
        Log.d("WritingScreen, Logged User?:", authState?.loggedUser.toString())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Leyendo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.Gray
                )
            )
        }
    ) { innerPadding ->

        Row(
            modifier = Modifier.padding(innerPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                isSpeaking = false
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp),
                    label = { Text("Escribe aquí lo que quieres leer en voz alta (generada)") },
                    maxLines = 10,
                    minLines = 10,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Default
                    )
                )

                Box(
                    modifier = Modifier.padding(top = 60.dp),
                ) {
                    Button(onClick = {
                        if (tts.value?.isSpeaking == true) {
                            tts.value?.stop()
                            isSpeaking = false
                        } else {
                            tts.value?.speak(
                                inputText, TextToSpeech.QUEUE_FLUSH, null, ""
                            )
                            isSpeaking = true
                        }
                    }) {
                        Text("Escuchar!")
                    }
                }
            }
        }
    }


}

@Composable
fun rememberTextToSpeech(): MutableState<TextToSpeech?> {
    val context = LocalContext.current
    val tts = remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(context) {
        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.value?.language = Locale.forLanguageTag("ES")
                tts.value?.setPitch(1f)
            }
        }
        tts.value = textToSpeech

        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }
    return tts
}