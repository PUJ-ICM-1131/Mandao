package com.uniruta.app.ui.screens.student

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uniruta.app.data.model.MockUser
import com.uniruta.app.ui.components.FeatureItem
import com.uniruta.app.ui.components.RoleHomeScaffold

private val studentFeatures = listOf(
    FeatureItem(
        title = "Recorridos disponibles",
        description = "Consulta las rutas y horarios habilitados para hoy."
    ),
    FeatureItem(
        title = "Mis reservas",
        description = "Revisa los cupos que ya tienes asegurados."
    ),
    FeatureItem(
        title = "Mi pase",
        description = "Presenta tu pase al abordar el vehículo."
    )
)

@Composable
fun StudentHomeScreen(
    user: MockUser,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    RoleHomeScaffold(
        user = user,
        headline = "Planea tu recorrido del día en el campus.",
        features = studentFeatures,
        onLogout = onLogout,
        modifier = modifier
    )
}
