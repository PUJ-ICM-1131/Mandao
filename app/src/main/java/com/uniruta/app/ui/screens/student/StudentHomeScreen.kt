package com.uniruta.app.ui.screens.student

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uniruta.app.data.model.MockUser
import com.uniruta.app.ui.components.FeatureItem
import com.uniruta.app.ui.components.RoleHomeScaffold

@Composable
fun StudentHomeScreen(
    user: MockUser,
    onOpenTrips: () -> Unit,
    onOpenReservations: () -> Unit,
    onOpenPass: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    RoleHomeScaffold(
        user = user,
        headline = "Planea tu recorrido del día en el campus.",
        features = listOf(
            FeatureItem(
                title = "Recorridos disponibles",
                description = "Consulta las rutas y horarios habilitados.",
                icon = Icons.Default.Place,
                onClick = onOpenTrips
            ),
            FeatureItem(
                title = "Mis reservas",
                description = "Revisa el estado de tus cupos y pagos.",
                icon = Icons.Default.DateRange,
                onClick = onOpenReservations
            ),
            FeatureItem(
                title = "Mi pase",
                description = "Presenta tu pase al abordar el vehículo.",
                icon = Icons.Default.AccountBox,
                onClick = onOpenPass
            )
        ),
        onLogout = onLogout,
        modifier = modifier
    )
}
