package com.uniruta.app.ui.screens.driver

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.model.Trip
import com.uniruta.app.ui.components.InfoRow
import com.uniruta.app.ui.components.SectionTitle
import com.uniruta.app.ui.components.StatusChip
import com.uniruta.app.ui.components.UniRutaTopBar
import com.uniruta.app.ui.components.tone
import com.uniruta.app.ui.format.asLongText
import com.uniruta.app.ui.format.asText

@Composable
fun TripFinishedScreen(
    trip: Trip,
    boardedCount: Int,
    passengerCount: Int,
    anomalyCount: Int,
    incidentCount: Int,
    onBackHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { UniRutaTopBar(title = "Recorrido finalizado") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
            )

            Text(text = "Recorrido finalizado", style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "Se detuvo el envío de ubicación y el monitoreo de movimiento.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            StatusChip(label = trip.status.label, tone = trip.status.tone())

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionTitle(text = "Resumen")
                    InfoRow(label = "Ruta", value = trip.route.name)
                    InfoRow(label = "Fecha", value = trip.date.asLongText())
                    InfoRow(label = "Hora de salida", value = trip.departureTime.asText())
                    InfoRow(
                        label = "Abordajes registrados",
                        value = "$boardedCount de $passengerCount"
                    )
                    InfoRow(label = "Movimientos anómalos", value = anomalyCount.toString())
                    InfoRow(label = "Novedades reportadas", value = incidentCount.toString())
                }
            }

            Button(
                onClick = onBackHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Volver al inicio")
            }
        }
    }
}
