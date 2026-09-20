package com.uniruta.app.ui.screens.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.model.Payment
import com.uniruta.app.data.model.Reservation
import com.uniruta.app.data.model.Trip
import com.uniruta.app.ui.components.StatusChip
import com.uniruta.app.ui.components.UniRutaTopBar
import com.uniruta.app.ui.components.tone
import com.uniruta.app.ui.format.asLongText
import com.uniruta.app.ui.format.asPriceText
import com.uniruta.app.ui.format.asText

data class ReservationRow(
    val reservation: Reservation,
    val trip: Trip?,
    val payment: Payment?
)

@Composable
fun StudentReservationsScreen(
    rows: List<ReservationRow>,
    onReservationSelected: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { UniRutaTopBar(title = "Mis reservas", onBack = onBack) }
    ) { innerPadding ->
        if (rows.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Todavía no tienes reservas. Empieza por consultar los recorridos disponibles.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
            return@Scaffold
        }

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
            items(rows, key = { it.reservation.id }) { row ->
                ReservationCard(row = row, onClick = { onReservationSelected(row.reservation.id) })
            }
        }
    }
}

@Composable
private fun ReservationCard(
    row: ReservationRow,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = row.trip?.route?.name ?: "Recorrido no disponible",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (row.trip != null) {
                Text(
                    text = "${row.trip.date.asLongText()} · ${row.trip.departureTime.asText()}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusChip(
                    label = "Reserva: ${row.reservation.status.label}",
                    tone = row.reservation.status.tone()
                )
                if (row.payment != null) {
                    StatusChip(
                        label = "Pago: ${row.payment.status.label}",
                        tone = row.payment.status.tone()
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = row.payment?.method?.label ?: "Sin método de pago",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = (row.payment?.amount ?: row.trip?.price ?: 0).asPriceText(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
