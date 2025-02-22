package com.nolineal.appskravetz

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.google.firebase.FirebaseApp
import com.nolineal.appskravetz.navigation.AppNavigator
import com.nolineal.appskravetz.ui.theme.AppSKravetzTheme

class MainActivity : ComponentActivity() {

    private var navController: NavHostController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        actionBar?.hide()
        setContent {
            AppSKravetzTheme {
                val navController = rememberNavController()
                this.navController = navController
                AppNavigator(actionBar, navController)
            }
        }
        }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                Log.d("MainActivity", "onOptionsItemSelected:")
                navController?.navigateUp() ?: false
                return true
            } else -> super.onOptionsItemSelected(item)
        }
    }


}
