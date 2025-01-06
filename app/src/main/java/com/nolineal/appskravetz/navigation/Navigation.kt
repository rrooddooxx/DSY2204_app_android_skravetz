package com.nolineal.appskravetz.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nolineal.appskravetz.data.AppData
import com.nolineal.appskravetz.screens.DashboardScreen
import com.nolineal.appskravetz.screens.LoginScreen
import com.nolineal.appskravetz.screens.RegisterScreen


@Composable
fun NavigationStack(
    navController: NavHostController,
    appData: AppData
) {
    NavHost(navController = navController, startDestination = Routes.LoginScreen) {
        composable(Routes.LoginScreen) { LoginScreen(navController, appData) }
        composable(Routes.RegisterScreen) { RegisterScreen(navController, appData) }
        composable(Routes.DashboardScreen) { DashboardScreen(navController, appData) }
    }
}

@Composable
fun AppNavigator() {
    val appDataManager = AppData()
    val navigationController = rememberNavController()
    NavigationStack(navigationController, appDataManager)
}