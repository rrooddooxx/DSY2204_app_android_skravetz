package com.nolineal.appskravetz.navigation

import android.app.ActionBar
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nolineal.appskravetz.R
import com.nolineal.appskravetz.screens.LoadingScreen
import com.nolineal.appskravetz.screens.private.DashboardScreen
import com.nolineal.appskravetz.screens.private.ListeningScreen
import com.nolineal.appskravetz.screens.private.WritingScreen
import com.nolineal.appskravetz.screens.public.ForgotPassword.ForgotPasswordScreen
import com.nolineal.appskravetz.screens.public.LoginScreen
import com.nolineal.appskravetz.screens.public.RegisterScreen
import com.nolineal.appskravetz.viewmodel.SharedAuthViewModel

@Composable
fun AppNavigator(
    actionBar: ActionBar?,
    navController: NavHostController,
    authViewModel: SharedAuthViewModel = viewModel(),
) {
    val loggedUser by authViewModel.authState.observeAsState()
    val isLoading by authViewModel.isLoadingState.observeAsState(initial = false)

    if (isLoading) {
        LoadingScreen()
    } else {
        if (loggedUser?.loggedUser == true) {
            actionBar?.hide()
            AuthenticatedNavigation(authViewModel, actionBar)
        } else {
            actionBar?.show()
            PublicNavigation(actionBar, authViewModel, navController)
        }
    }
}

@Composable
fun PublicNavigation(
    actionBar: ActionBar?,
    authViewModel: SharedAuthViewModel,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LoginScreen
    ) {
        composable(Routes.LoginScreen) {
            actionBar?.hide()
            LoginScreen(navController, authViewModel)
        }
        composable(Routes.RegisterScreen) {
            actionBar?.hide()
            RegisterScreen(navController, authViewModel)
        }
        composable(Routes.ForgotPasswordScreen) {
            actionBar?.hide()
            ForgotPasswordScreen(navController, authViewModel)
        }
    }
}

@Composable
fun AuthenticatedNavigation(
    authViewModel: SharedAuthViewModel,
    actionBar: ActionBar?,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.DashboardScreen
    ) {
        composable(Routes.DashboardScreen) {
            actionBar?.show()
            actionBar?.setDisplayHomeAsUpEnabled(false)
            actionBar?.setDisplayShowHomeEnabled(true)
            DashboardScreen(navController, authViewModel)
        }
        composable(Routes.WritingScreen) {
            actionBar?.show()
            actionBar?.setDisplayHomeAsUpEnabled(true)
            actionBar?.setHomeAsUpIndicator(R.drawable.back_arrow2)
            actionBar?.setHomeButtonEnabled(true)
            actionBar?.setDisplayShowHomeEnabled(false)
            WritingScreen(navController, authViewModel)
        }
        composable(Routes.ListeningScreen) {
            actionBar?.show()
            actionBar?.setDisplayHomeAsUpEnabled(true)
            actionBar?.setHomeAsUpIndicator(R.drawable.back_arrow2)
            actionBar?.setHomeButtonEnabled(true)
            actionBar?.setDisplayShowHomeEnabled(false)

            ListeningScreen(navController, authViewModel)
        }
    }

    BackHandler(
        onBack = {
            navController.navigateUp()
        },
        enabled = true
    )
}

