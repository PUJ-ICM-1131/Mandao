package com.uniruta.app.ui.screens.student

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.model.Trip
import com.uniruta.app.ui.components.InfoRow
import com.uniruta.app.ui.components.RoutePreview
import com.uniruta.app.ui.components.SectionTitle
import com.uniruta.app.ui.components.StatusChip
import com.uniruta.app.ui.components.StatusTone
import com.uniruta.app.ui.components.UniRutaTopBar
import com.uniruta.app.ui.format.asDistanceText
import com.uniruta.app.ui.format.asDurationText
import com.uniruta.app.ui.format.asLongText
import com.uniruta.app.ui.format.asPriceText
import com.uniruta.app.ui.format.asText

@Composable
fun TripDetailScreen(
    trip: Trip,
    alreadyReserved: Boolean,
    onReserve: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                        label = if (trip.isBookable) "Disponible" else "Sin cupos",
                        tone = if (trip.isBookable) StatusTone.POSITIVE else StatusTone.NEGATIVE,
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
                                label = "Conductor",
                                value = trip.driverName,
                                modifier = Modifier.weight(1f)
                            )
                            InfoRow(
                                label = "Cupos",
                                value = "${trip.availableSeats} de ${trip.totalSeats}",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            InfoRow(
                                label = "Duración",
                                value = trip.estimatedDurationMinutes.asDurationText(),
                                modifier = Modifier.weight(1f)
                            )
                            InfoRow(
                                label = "Distancia",
                                value = trip.estimatedDistanceKm.asDistanceText(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        InfoRow(label = "Precio por cupo", value = trip.price.asPriceText())
                    }
                }
            }

            item {
                Card(
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
                Column {
                    if (alreadyReserved) {
                        Text(
                            text = "Ya tienes una reserva activa para este recorrido.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Button(
                        onClick = onReserve,
                        enabled = trip.isBookable && !alreadyReserved,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Reservar cupo")
                    }
                }
            }
        }
    }
}
