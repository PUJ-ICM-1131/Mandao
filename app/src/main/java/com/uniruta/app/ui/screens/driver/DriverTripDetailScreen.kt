package com.uniruta.app.ui.screens.driver

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.model.Trip
import com.uniruta.app.data.model.TripStatus
import com.uniruta.app.ui.components.InfoRow
import com.uniruta.app.ui.components.RoutePreview
import com.uniruta.app.ui.components.SectionTitle
import com.uniruta.app.ui.components.StatusChip
import com.uniruta.app.ui.components.UniRutaTopBar
import com.uniruta.app.ui.components.tone
import com.uniruta.app.ui.format.asLongText
import com.uniruta.app.ui.format.asText

@Composable
fun DriverTripDetailScreen(
    trip: Trip,
    passengerCount: Int,
    startBlockedReason: String?,
    onStartTrip: () -> Unit,
    onOpenActiveTrip: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var confirmVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { UniRutaTopBar(title = "Detalle del recorrido", onBack = onBack) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 28.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    Text(
                        text = trip.route.name,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "${trip.route.origin} → ${trip.route.destination}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    StatusChip(
                        label = trip.status.label,
                        tone = trip.status.tone(),
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }

            item {
                RoutePreview(
                    origin = trip.route.origin,
                    destination = trip.route.destination,
                    stopCount = trip.route.stops.size
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SectionTitle(text = "Información del recorrido")
                        InfoRow(label = "Fecha", value = trip.date.asLongText())
                        Row(modifier = Modifier.fillMaxWidth()) {
                            InfoRow(
                                label = "Hora de salida",
                                value = trip.departureTime.asText(),
                                modifier = Modifier.weight(1f)
                            )
                            InfoRow(
                                label = "Vehículo",
                                value = trip.vehiclePlate,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            InfoRow(
                                label = "Capacidad",
                                value = "${trip.totalSeats} puestos",
                                modifier = Modifier.weight(1f)
                            )
                            InfoRow(
                                label = "Pasajeros",
                                value = "$passengerCount reservas",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SectionTitle(text = "Paradas")
                        trip.route.stops.sortedBy { it.order }.forEach { stop ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    shape = CircleShape,
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = stop.order.toString(),
                                            style = MaterialTheme.typography.labelMedium,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                                Text(
                                    text = stop.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                when {
                    trip.status == TripStatus.IN_PROGRESS -> {
                        Button(
                            onClick = onOpenActiveTrip,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Ir al recorrido en curso")
                        }
                    }
                    trip.status == TripStatus.SCHEDULED -> {
                        Column {
                            if (startBlockedReason != null) {
                                Text(
                                    text = startBlockedReason,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            Button(
                                onClick = { confirmVisible = true },
                                enabled = startBlockedReason == null,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Iniciar recorrido")
                            }
                        }
                    }
                    else -> {
                        Text(
                            text = "Este recorrido ya no admite acciones.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    if (confirmVisible) {
        AlertDialog(
            onDismissRequest = { confirmVisible = false },
            title = { Text(text = "¿Iniciar recorrido?") },
            text = {
                Text(
                    text = "Al iniciar comenzarás a compartir tu ubicación y se activará el " +
                        "monitoreo de movimiento durante todo el trayecto."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmVisible = false
                        onStartTrip()
                    }
                ) {
                    Text(text = "Iniciar")
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmVisible = false }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }
}
