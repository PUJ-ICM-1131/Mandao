package com.uniruta.app.ui.screens.admin

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uniruta.app.data.model.MockUser
import com.uniruta.app.ui.components.FeatureItem
import com.uniruta.app.ui.components.Metric
import com.uniruta.app.ui.components.MetricGrid
import com.uniruta.app.ui.components.RoleHomeScaffold

@Composable
fun AdminHomeScreen(
    user: MockUser,
    activeRouteCount: Int,
    activeVehicleCount: Int,
    scheduledTripCount: Int,
    pendingReviewCount: Int,
    onOpenRoutes: () -> Unit,
    onOpenVehicles: () -> Unit,
    onOpenTrips: () -> Unit,
    onOpenPayments: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    RoleHomeScaffold(
        user = user,
        headline = "Supervisa la operación del servicio de rutas.",
        features = listOf(
            FeatureItem(
                title = "Rutas",
                description = "Define los trayectos y paradas del servicio.",
                icon = Icons.Default.Place,
                onClick = onOpenRoutes
            ),
            FeatureItem(
                title = "Vehículos",
                description = "Administra la flota y su capacidad.",
                icon = Icons.Default.Settings,
                onClick = onOpenVehicles
            ),
            FeatureItem(
                title = "Recorridos",
                description = "Programa horarios y asigna conductores.",
                icon = Icons.Default.DateRange,
                onClick = onOpenTrips
            ),
            FeatureItem(
                title = "Pagos",
                description = "Supervisa los pagos y el estado de los pases.",
                icon = Icons.Default.ShoppingCart,
                onClick = onOpenPayments
            )
        ),
        onLogout = onLogout,
        modifier = modifier,
        header = {
            MetricGrid(
                metrics = listOf(
                    Metric(label = "Rutas activas", value = activeRouteCount),
                    Metric(label = "Vehículos activos", value = activeVehicleCount),
                    Metric(label = "Recorridos programados", value = scheduledTripCount),
                    Metric(
                        label = "Pagos por revisar",
                        value = pendingReviewCount,
                        highlighted = pendingReviewCount > 0
                    )
                )
            )
        }
    )
}
