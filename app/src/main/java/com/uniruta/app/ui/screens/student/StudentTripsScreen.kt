package com.uniruta.app.ui.screens.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.model.Trip
import com.uniruta.app.ui.components.StatusChip
import com.uniruta.app.ui.components.StatusTone
import com.uniruta.app.ui.components.UniRutaTopBar
import com.uniruta.app.ui.format.asDistanceText
import com.uniruta.app.ui.format.asDurationText
import com.uniruta.app.ui.format.asLongText
import com.uniruta.app.ui.format.asPriceText
import com.uniruta.app.ui.format.asText

@Composable
fun StudentTripsScreen(
    trips: List<Trip>,
    onTripSelected: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { UniRutaTopBar(title = "Recorridos disponibles", onBack = onBack) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(trips, key = { it.id }) { trip ->
                TripCard(trip = trip, onClick = { onTripSelected(trip.id) })
            }
        }
    }
}

@Composable
private fun TripCard(
    trip: Trip,
    onClick: () -> Unit
) {
    val available = trip.isBookable
    val cardModifier = Modifier
        .fillMaxWidth()
        .let { if (available) it.clickable(onClick = onClick) else it }

    Card(
        modifier = cardModifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = trip.route.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (available) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                    Text(
                        text = "${trip.route.origin} → ${trip.route.destination}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                StatusChip(
                    label = if (available) "Disponible" else "Sin cupos",
                    tone = if (available) StatusTone.POSITIVE else StatusTone.NEGATIVE
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TripFact(
                    label = "Fecha",
                    value = trip.date.asLongText(),
                    modifier = Modifier.weight(1.4f)
                )
                TripFact(
                    label = "Salida",
                    value = trip.departureTime.asText(),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                TripFact(
                    label = "Cupos",
                    value = "${trip.availableSeats} / ${trip.totalSeats}",
                    modifier = Modifier.weight(1f)
                )
                TripFact(
                    label = "Duración",
                    value = trip.estimatedDurationMinutes.asDurationText(),
                    modifier = Modifier.weight(1f)
                )
                TripFact(
                    label = "Distancia",
                    value = trip.estimatedDistanceKm.asDistanceText(),
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = trip.price.asPriceText(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = if (available) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
private fun TripFact(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
