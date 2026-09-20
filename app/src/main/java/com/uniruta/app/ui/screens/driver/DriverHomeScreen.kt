package com.uniruta.app.ui.screens.driver

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uniruta.app.data.model.MockUser
import com.uniruta.app.ui.components.FeatureItem
import com.uniruta.app.ui.components.RoleHomeScaffold

@Composable
fun DriverHomeScreen(
    user: MockUser,
    hasActiveTrip: Boolean,
    onOpenTrips: () -> Unit,
    onStartTrip: () -> Unit,
    onValidateBoarding: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    RoleHomeScaffold(
        user = user,
        headline = "Gestiona los recorridos que tienes a cargo.",
        features = listOf(
            FeatureItem(
                title = "Recorridos asignados",
                description = "Revisa las rutas que tienes programadas.",
                icon = Icons.Default.Place,
                onClick = onOpenTrips
            ),
            FeatureItem(
                title = if (hasActiveTrip) "Recorrido en curso" else "Iniciar recorrido",
                description = if (hasActiveTrip) {
                    "Continúa el seguimiento del recorrido activo."
                } else {
                    "Marca la salida y comparte tu ubicación con los estudiantes."
                },
                icon = Icons.Default.PlayArrow,
                onClick = onStartTrip
            ),
            FeatureItem(
                title = "Validar abordaje",
                description = "Verifica el pase de cada estudiante que sube al vehículo.",
                icon = Icons.Default.CheckCircle,
                onClick = onValidateBoarding
            )
        ),
        onLogout = onLogout,
        modifier = modifier
    )
}
