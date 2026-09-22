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
import com.uniruta.app.ui.screens.auth.LoginScreen
import com.uniruta.app.viewmodel.AdminViewModel
import com.uniruta.app.viewmodel.AuthViewModel
import com.uniruta.app.viewmodel.DriverViewModel
import com.uniruta.app.viewmodel.StudentViewModel

@Composable
fun UniRutaApp(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(),
    studentViewModel: StudentViewModel = viewModel(),
    driverViewModel: DriverViewModel = viewModel(),
    adminViewModel: AdminViewModel = viewModel()
) {
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    val studentState by studentViewModel.uiState.collectAsStateWithLifecycle()
    val driverState by driverViewModel.uiState.collectAsStateWithLifecycle()
    val adminState by adminViewModel.uiState.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(Login)
    val currentUser = authState.currentUser

    LaunchedEffect(currentUser?.role) {
        if (currentUser == null) {
            studentViewModel.reset()
            driverViewModel.reset()
            adminViewModel.reset()
        }
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
                    state = authState,
                    onEmailChange = authViewModel::onEmailChange,
                    onPasswordChange = authViewModel::onPasswordChange,
                    onSubmit = authViewModel::login,
                    onDemoLogin = authViewModel::loginAs
                )
            }

            studentEntries(
                state = studentState,
                viewModel = studentViewModel,
                user = currentUser,
                onLogout = authViewModel::logout,
                backStack = backStack
            )

            driverEntries(
                state = driverState,
                viewModel = driverViewModel,
                user = currentUser,
                onLogout = authViewModel::logout,
                backStack = backStack
            )

            adminEntries(
                state = adminState,
                viewModel = adminViewModel,
                user = currentUser,
                onLogout = authViewModel::logout,
                backStack = backStack
            )
        }
    )
}
