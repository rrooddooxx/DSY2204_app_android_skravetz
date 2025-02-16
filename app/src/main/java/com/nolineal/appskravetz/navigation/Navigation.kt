package com.nolineal.appskravetz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nolineal.appskravetz.screens.private.DashboardScreen
import com.nolineal.appskravetz.screens.public.ForgotPassword.ForgotPasswordScreen
import com.nolineal.appskravetz.screens.public.LoginScreen
import com.nolineal.appskravetz.screens.public.RegisterScreen
import com.nolineal.appskravetz.viewmodel.SharedAuthViewModel


@Composable
fun NavigationStack(
    navController: NavHostController,
    authViewModel: SharedAuthViewModel,
) {
    val isLoggedUser =
        authViewModel.authState.observeAsState().value?.loggedUser

    NavHost(
        navController = navController,
        startDestination = if (isLoggedUser != null && isLoggedUser) Routes.DashboardScreen else Routes.LoginScreen
    ) {
        // stack "public" (no necesita auth)
        composable(Routes.LoginScreen) { LoginScreen(navController, authViewModel) }
        composable(Routes.RegisterScreen) { RegisterScreen(navController, authViewModel) }
        composable(Routes.ForgotPasswordScreen) {
            ForgotPasswordScreen(
                navController,
                authViewModel
            )
        }

        // stack "private" (necesita auth)
        composable(Routes.DashboardScreen) { DashboardScreen(navController, authViewModel) }
    }
}

@Composable
fun AppNavigator(
    authViewModel: SharedAuthViewModel = viewModel()
) {
    val navigationController = rememberNavController()
    NavigationStack(navigationController, authViewModel)
}