package com.nolineal.appskravetz.screens.public

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nolineal.appskravetz.domain.LoginFormStates
import com.nolineal.appskravetz.navigation.Routes
import com.nolineal.appskravetz.ui.theme.AppSKravetzTheme
import com.nolineal.appskravetz.viewmodel.SharedAuthViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`


@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get: Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockNavController: NavHostController
    private lateinit var mockAuthViewModel: SharedAuthViewModel

    @Before
    fun setup() {
        mockNavController = mock(NavHostController::class.java)
        mockAuthViewModel = mock(SharedAuthViewModel::class.java)
    }

    @Test
    fun testLoginScreen() {
        composeTestRule.setContent {
            AppSKravetzTheme {
                LoginScreen(mockNavController, mockAuthViewModel)
            }
        }
    }

    @Test
    fun testEmailPasswordVacios() {
        `when`(mockAuthViewModel.loginFormState).thenReturn(MutableLiveData(LoginFormStates.INITIAL))

        composeTestRule.setContent {
            AppSKravetzTheme {
                LoginScreen(mockNavController, mockAuthViewModel)
            }
        }
        composeTestRule.onNodeWithText("Iniciar Sesión").performClick()

        verify(mockAuthViewModel).setLoginFormState(LoginFormStates.EMPTY)
        composeTestRule.onNodeWithText("Por favor, ingresa tus datos").assertExists()
    }

    @Test
    fun testLoginExitoso() {
        val email = "test@example.com"
        val password = "password"
        `when`(mockAuthViewModel.loginFormState).thenReturn(MutableLiveData(LoginFormStates.SUCCESS))

        composeTestRule.setContent {
            AppSKravetzTheme {
                LoginScreen(mockNavController, mockAuthViewModel)
            }
        }
        composeTestRule.onNodeWithText("E-Mail").performTextInput(email)
        composeTestRule.onNodeWithText("Contraseña").performTextInput(password)
        composeTestRule.onNodeWithText("Iniciar Sesión").performClick()

        verify(mockAuthViewModel).login(email, password)
        verify(mockNavController).navigate(Routes.DashboardScreen)
        verify(mockAuthViewModel).setLoadingState(false)
    }
}