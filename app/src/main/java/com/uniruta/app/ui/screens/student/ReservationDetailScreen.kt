package com.uniruta.app.ui.screens.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniruta.app.ui.components.InfoRow
import com.uniruta.app.ui.components.SectionTitle
import com.uniruta.app.ui.components.StatusChip
import com.uniruta.app.ui.components.UniRutaTopBar
import com.uniruta.app.ui.components.tone
import com.uniruta.app.ui.format.asLongText
import com.uniruta.app.ui.format.asPriceText
import com.uniruta.app.ui.format.asShortText
import com.uniruta.app.ui.format.asText

@Composable
fun ReservationDetailScreen(
    row: ReservationRow,
    passAvailable: Boolean,
    onOpenPass: () -> Unit,
    onCancelReservation: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val reservation = row.reservation
    val trip = row.trip
    val payment = row.payment

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { UniRutaTopBar(title = "Detalle de la reserva", onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusChip(
                    label = "Reserva: ${reservation.status.label}",
                    tone = reservation.status.tone()
                )
                if (payment != null) {
                    StatusChip(
                        label = "Pago: ${payment.status.label}",
                        tone = payment.status.tone()
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionTitle(text = "Recorrido")
                    if (trip == null) {
                        Text(
                            text = "El recorrido asociado ya no está disponible.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        InfoRow(label = "Ruta", value = trip.route.name)
                        InfoRow(label = "Fecha", value = trip.date.asLongText())
                        InfoRow(label = "Hora de salida", value = trip.departureTime.asText())
                        InfoRow(label = "Vehículo", value = trip.vehiclePlate)
                        InfoRow(label = "Conductor", value = trip.driverName)
                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionTitle(text = "Reserva y pago")
                    InfoRow(label = "Identificador", value = reservation.id)
                    InfoRow(label = "Fecha de reserva", value = reservation.reservationDate.asShortText())
                    InfoRow(
                        label = "Método de pago",
                        value = payment?.method?.label ?: "Sin seleccionar"
                    )
                    InfoRow(
                        label = "Valor",
                        value = (payment?.amount ?: trip?.price ?: 0).asPriceText()
                    )
                    if (payment?.receiptName != null) {
                        InfoRow(label = "Comprobante", value = payment.receiptName)
                    }
                }
            }

            if (passAvailable) {
                Button(
                    onClick = onOpenPass,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Ver pase digital")
                }
            }

            if (reservation.isActive) {
                OutlinedButton(
                    onClick = onCancelReservation,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Cancelar reserva")
                }
            }
        }
    }
}
