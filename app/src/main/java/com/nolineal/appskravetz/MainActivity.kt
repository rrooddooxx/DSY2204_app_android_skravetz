package com.nolineal.appskravetz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.FirebaseApp
import com.nolineal.appskravetz.navigation.AppNavigator
import com.nolineal.appskravetz.ui.theme.AppSKravetzTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        actionBar?.hide()
        setContent {
            AppSKravetzTheme {
                AppNavigator()
            }
        }
    }
}
