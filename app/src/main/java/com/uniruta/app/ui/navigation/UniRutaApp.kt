package com.uniruta.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.uniruta.app.ui.screens.admin.AdminHomeScreen
import com.uniruta.app.ui.screens.auth.LoginScreen
import com.uniruta.app.ui.screens.driver.DriverHomeScreen
import com.uniruta.app.ui.screens.student.StudentHomeScreen
import com.uniruta.app.viewmodel.AuthViewModel

@Composable
fun UniRutaApp(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel()
) {
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(Login)
    val currentUser = uiState.currentUser

    LaunchedEffect(currentUser?.role) {
        val destination = currentUser?.role?.let(::homeDestinationFor) ?: Login
        if (backStack.lastOrNull() != destination) {
            backStack.clear()
            backStack.add(destination)
        }
    }

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Login> {
                LoginScreen(
                    state = uiState,
                    onEmailChange = authViewModel::onEmailChange,
                    onPasswordChange = authViewModel::onPasswordChange,
                    onSubmit = authViewModel::login,
                    onDemoLogin = authViewModel::loginAs
                )
            }
            entry<StudentHome> {
                currentUser?.let { StudentHomeScreen(user = it, onLogout = authViewModel::logout) }
            }
            entry<DriverHome> {
                currentUser?.let { DriverHomeScreen(user = it, onLogout = authViewModel::logout) }
            }
            entry<AdminHome> {
                currentUser?.let { AdminHomeScreen(user = it, onLogout = authViewModel::logout) }
            }
        }
    )
}
