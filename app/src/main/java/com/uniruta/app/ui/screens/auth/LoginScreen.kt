package com.uniruta.app.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.mock.MockAccounts
import com.uniruta.app.data.model.UserRole
import com.uniruta.app.viewmodel.AuthUiState

@Composable
fun LoginScreen(
    state: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onDemoLogin: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 28.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "UniRuta",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Tu ruta al campus, en un solo lugar.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 36.dp)
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = { Text(text = "Correo institucional") },
                singleLine = true,
                isError = state.errorMessage != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                label = { Text(text = "Contraseña") },
                singleLine = true,
                isError = state.errorMessage != null,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )

            val errorMessage = state.errorMessage
            if (errorMessage != null) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                    )
                }
            }

            Button(
                onClick = onSubmit,
                enabled = state.canSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text(text = "Iniciar sesión")
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 28.dp))

            DemoAccess(enabled = !state.isLoading, onDemoLogin = onDemoLogin)
        }
    }
}

@Composable
private fun DemoAccess(
    enabled: Boolean,
    onDemoLogin: (UserRole) -> Unit
) {
    var credentialsVisible by rememberSaveable { mutableStateOf(false) }

    Text(
        text = "Acceso rápido de demostración",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        UserRole.entries.forEach { role ->
            FilledTonalButton(
                onClick = { onDemoLogin(role) },
                enabled = enabled,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = role.label,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1
                )
            }
        }
    }

    TextButton(onClick = { credentialsVisible = !credentialsVisible }) {
        Text(
            text = if (credentialsVisible) "Ocultar cuentas de prueba" else "Ver cuentas de prueba",
            style = MaterialTheme.typography.labelMedium
        )
    }

    if (credentialsVisible) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                MockAccounts.all.forEach { account ->
                    Text(
                        text = "${account.role.label}: ${account.email}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    text = "Contraseña para todas: ${MockAccounts.DEMO_PASSWORD}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}
