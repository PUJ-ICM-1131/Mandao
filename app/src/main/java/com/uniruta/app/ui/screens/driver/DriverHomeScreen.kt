package com.uniruta.app.ui.screens.driver

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uniruta.app.data.model.MockUser
import com.uniruta.app.ui.components.FeatureItem
import com.uniruta.app.ui.components.RoleHomeScaffold

private val driverFeatures = listOf(
    FeatureItem(
        title = "Recorridos asignados",
        description = "Revisa las rutas que tienes programadas."
    ),
    FeatureItem(
        title = "Iniciar recorrido",
        description = "Marca la salida y comparte tu ubicación con los estudiantes."
    ),
    FeatureItem(
        title = "Validar abordaje",
        description = "Verifica el pase de cada estudiante que sube al vehículo."
    )
)

@Composable
fun DriverHomeScreen(
    user: MockUser,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    RoleHomeScaffold(
        user = user,
        headline = "Gestiona los recorridos que tienes a cargo.",
        features = driverFeatures,
        onLogout = onLogout,
        modifier = modifier
    )
}
