package com.uniruta.app.ui.screens.driver

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.model.AnomalyEvent
import com.uniruta.app.data.model.TrackingStatus
import com.uniruta.app.data.model.Trip
import com.uniruta.app.ui.components.SectionTitle
import com.uniruta.app.ui.components.StatusChip
import com.uniruta.app.ui.components.StatusTile
import com.uniruta.app.ui.components.StatusTone
import com.uniruta.app.ui.components.UniRutaTopBar
import com.uniruta.app.ui.components.tone
import com.uniruta.app.ui.format.asText

@Composable
fun ActiveTripScreen(
    trip: Trip,
    gpsStatus: TrackingStatus,
    sensorStatus: TrackingStatus,
    boardedCount: Int,
    passengerCount: Int,
    anomalies: List<AnomalyEvent>,
    incidentCount: Int,
    onValidateBoarding: () -> Unit,
    onOpenPassengers: () -> Unit,
    onReportIncident: () -> Unit,
    onFinishTrip: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var confirmVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { UniRutaTopBar(title = "Recorrido en curso", onBack = onBack) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 28.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = trip.route.name,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Column {
                                Text(
                                    text = "SALIDA",
                                    style = MaterialTheme.typography.labelSmall
                                )
                                Text(
                                    text = trip.departureTime.asText(),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Column {
                                Text(
                                    text = "VEHÍCULO",
                                    style = MaterialTheme.typography.labelSmall
                                )
                                Text(
                                    text = trip.vehiclePlate,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        StatusChip(
                            label = trip.status.label,
                            tone = trip.status.tone(),
                            modifier = Modifier.padding(top = 14.dp)
                        )
                    }
                }
            }

            item {
                StatusTile(
                    icon = Icons.Default.LocationOn,
                    title = "Ubicación",
                    detail = "Compartiendo ubicación",
                    chipLabel = gpsStatus.label,
                    chipTone = gpsStatus.tone()
                )
            }

            item {
                StatusTile(
                    icon = Icons.Default.Settings,
                    title = "Sensores",
                    detail = "Acelerómetro activo · Giroscopio activo",
                    chipLabel = sensorStatus.label,
                    chipTone = sensorStatus.tone()
                )
            }

            item {
                StatusTile(
                    icon = Icons.Default.CheckCircle,
                    title = "Abordaje",
                    detail = "$boardedCount de $passengerCount pasajeros abordados",
                    chipLabel = if (boardedCount == passengerCount) "Completo" else "En curso",
                    chipTone = if (boardedCount == passengerCount) StatusTone.POSITIVE else StatusTone.NEUTRAL
                )
            }

            item {
                StatusTile(
                    icon = Icons.Default.Warning,
                    title = "Movimientos anómalos",
                    detail = "${anomalies.size} eventos registrados",
                    chipLabel = if (anomalies.isEmpty()) "Sin eventos" else "Revisar",
                    chipTone = if (anomalies.isEmpty()) StatusTone.NEUTRAL else StatusTone.WARNING
                )
            }

            if (anomalies.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            SectionTitle(text = "Registro de movimiento")
                            anomalies.forEach { anomaly ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = anomaly.type.label,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = anomaly.type.source.label,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                    Text(
                                        text = anomaly.time.asText(),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Button(
                        onClick = onValidateBoarding,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Validar abordaje")
                    }
                    OutlinedButton(
                        onClick = onOpenPassengers,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text(text = "Ver pasajeros")
                    }
                    OutlinedButton(
                        onClick = onReportIncident,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = if (incidentCount == 0) {
                                "Reportar novedad"
                            } else {
                                "Reportar novedad ($incidentCount)"
                            }
                        )
                    }
                    OutlinedButton(
                        onClick = { confirmVisible = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text(text = "Finalizar recorrido")
                    }
                }
            }
        }
    }

    if (confirmVisible) {
        AlertDialog(
            onDismissRequest = { confirmVisible = false },
            title = { Text(text = "¿Finalizar recorrido?") },
            text = {
                Text(
                    text = "Se detendrá el envío de ubicación y el monitoreo de movimiento. " +
                        "No podrás seguir validando abordajes de este recorrido."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmVisible = false
                        onFinishTrip()
                    }
                ) {
                    Text(text = "Finalizar")
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
