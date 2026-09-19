package com.uniruta.app.ui.screens.admin

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uniruta.app.data.model.MockUser
import com.uniruta.app.ui.components.FeatureItem
import com.uniruta.app.ui.components.RoleHomeScaffold

private val adminFeatures = listOf(
    FeatureItem(
        title = "Rutas",
        description = "Define los trayectos y paradas del servicio."
    ),
    FeatureItem(
        title = "Vehículos",
        description = "Administra la flota y su capacidad."
    ),
    FeatureItem(
        title = "Recorridos",
        description = "Programa horarios y asigna conductores."
    ),
    FeatureItem(
        title = "Pagos",
        description = "Supervisa los pagos y el estado de los pases."
    )
)

@Composable
fun AdminHomeScreen(
    user: MockUser,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    RoleHomeScaffold(
        user = user,
        headline = "Supervisa la operación del servicio de rutas.",
        features = adminFeatures,
        onLogout = onLogout,
        modifier = modifier
    )
}
