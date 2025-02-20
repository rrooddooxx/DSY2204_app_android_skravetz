package com.nolineal.appskravetz

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.nolineal.appskravetz.navigation.AppNavigator
import com.nolineal.appskravetz.ui.theme.AppSKravetzTheme

class MainActivity : ComponentActivity() {

    private var navController: NavHostController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        when (item.itemId) {
            android.R.id.home -> {
                navController?.navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
