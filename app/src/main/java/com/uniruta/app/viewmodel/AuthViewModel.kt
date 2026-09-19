package com.uniruta.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniruta.app.data.mock.MockAccounts
import com.uniruta.app.data.model.MockUser
import com.uniruta.app.data.model.UserRole
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val currentUser: MockUser? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val canSubmit: Boolean
        get() = !isLoading && email.isNotBlank() && password.isNotBlank()
}

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun login() {
        val state = _uiState.value
        if (!state.canSubmit) return
        authenticate(state.email, state.password)
    }

    fun loginAs(role: UserRole) {
        if (_uiState.value.isLoading) return
        val account = MockAccounts.findBy(role)
        _uiState.update { it.copy(email = account.email, password = MockAccounts.DEMO_PASSWORD) }
        authenticate(account.email, MockAccounts.DEMO_PASSWORD)
    }

    fun logout() {
        _uiState.value = AuthUiState()
    }

    private fun authenticate(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            delay(FAKE_NETWORK_DELAY_MS)
            val user = MockAccounts.findBy(email, password)
            _uiState.update { state ->
                if (user == null) {
                    state.copy(
                        isLoading = false,
                        errorMessage = "Correo o contraseña incorrectos"
                    )
                } else {
                    state.copy(isLoading = false, password = "", currentUser = user)
                }
            }
        }
    }

    private companion object {
        const val FAKE_NETWORK_DELAY_MS = 600L
    }
}
