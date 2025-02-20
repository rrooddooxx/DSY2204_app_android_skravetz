package com.nolineal.appskravetz.screens.private

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.nolineal.appskravetz.R
import com.nolineal.appskravetz.viewmodel.SharedAuthViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListeningScreen(
    navigation: NavHostController,
    authViewModel: SharedAuthViewModel
) {
    val currentUser = authViewModel.authState.observeAsState().value?.currentUser?.userData
    val authState = authViewModel.authState.observeAsState().value

    val context = LocalContext.current
    var prompt by remember { mutableStateOf("Prompt") }

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val spokenText =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (spokenText != null) {
                prompt = spokenText  // actualiza el prompt con el texto hablado
            } else {
                Toast.makeText(
                    context,
                    "Error al reconocer voz, inténtelo nuevamente.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        Log.d("WritingScreen", "...")
        Log.d("WritingScreen, Logged User?:", authState?.loggedUser.toString())
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escuchando") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.Gray
                )
            )
        }

    ) { innerPadding ->

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(innerPadding),

            ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = innerPadding.calculateTopPadding().times(0.8f))
                    .padding(bottom = innerPadding.calculateBottomPadding().times(1f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    IconButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        onClick = {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                                intent.putExtra(
                                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
                                )
                                intent.putExtra(
                                    RecognizerIntent.EXTRA_LANGUAGE,
                                    Locale.getDefault()
                                )
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-CL")
                                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hable ahora...")

                                speechRecognizerLauncher.launch(intent)

                            } else {
                                ActivityCompat.requestPermissions(
                                    context as Activity,
                                    arrayOf(Manifest.permission.RECORD_AUDIO),
                                    100
                                )
                            }
                        },
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(24.dp)
                                .width(140.dp)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = CircleShape
                                )

                        ) {
                            Box(modifier = Modifier.padding(32.dp)) {
                                Icon(
                                    painter = painterResource(id = R.drawable.microphone_icon),
                                    contentDescription = "Hablar",
                                )
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    Text(
                        text = "Aquí verás el texto reconocido:",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 48.dp)
                    )
                }


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .weight(2f)
                        .align(Alignment.CenterHorizontally)
                        .padding(40.dp),
                    contentAlignment = Alignment.TopCenter
                ) {


                    Box(
                        modifier = Modifier
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(60.dp),
                    ) {
                        Text(
                            text = prompt,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(0.dp),
                            lineHeight = TextUnit(20f, TextUnitType.Sp),
                            letterSpacing = TextUnit(0.3f, TextUnitType.Sp),
                        )
                    }

                }
            }

        }
    }
}
