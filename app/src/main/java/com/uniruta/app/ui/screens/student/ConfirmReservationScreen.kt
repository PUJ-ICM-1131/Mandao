package com.uniruta.app.ui.screens.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.model.Trip
import com.uniruta.app.ui.components.InfoRow
import com.uniruta.app.ui.components.SectionTitle
import com.uniruta.app.ui.components.UniRutaTopBar
import com.uniruta.app.ui.format.asLongText
import com.uniruta.app.ui.format.asPriceText
import com.uniruta.app.ui.format.asText

@Composable
fun ConfirmReservationScreen(
    trip: Trip,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { UniRutaTopBar(title = "Confirmar reserva", onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionTitle(text = "Resumen")
                    InfoRow(label = "Recorrido", value = trip.route.name)
                    InfoRow(label = "Fecha", value = trip.date.asLongText())
                    InfoRow(label = "Hora de salida", value = trip.departureTime.asText())
                    InfoRow(label = "Cupo", value = "1 cupo")
                    InfoRow(label = "Total a pagar", value = trip.price.asPriceText())
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionTitle(text = "Paradas principales")
                    trip.route.stops.sortedBy { it.order }.take(3).forEach { stop ->
                        Text(
                            text = "${stop.order}. ${stop.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 3.dp)
                        )
                    }
                    if (trip.route.stops.size > 3) {
                        Text(
                            text = "y ${trip.route.stops.size - 3} paradas más",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Al reservar, tu cupo quedará apartado mientras completas el proceso de pago.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Confirmar reserva")
            }

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cancelar")
            }
        }
    }
}
