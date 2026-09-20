package com.uniruta.app.ui.screens.driver

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.model.TripPassenger
import com.uniruta.app.ui.components.StatusChip
import com.uniruta.app.ui.components.StatusTone
import com.uniruta.app.ui.components.UniRutaTopBar
import com.uniruta.app.ui.components.tone
import com.uniruta.app.ui.format.asPriceText

@Composable
fun DriverPassengersScreen(
    passengers: List<TripPassenger>,
    onConfirmCashPayment: (String) -> Unit,
    onRegisterBoarding: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var cashDialogFor by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { UniRutaTopBar(title = "Pasajeros", onBack = onBack) }
    ) { innerPadding ->
        if (passengers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Este recorrido todavía no tiene pasajeros registrados.",
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
            items(passengers, key = { it.reservation.id }) { passenger ->
                PassengerCard(
                    passenger = passenger,
                    onConfirmCash = { cashDialogFor = passenger.reservation.id },
                    onRegisterBoarding = { onRegisterBoarding(passenger.reservation.id) }
                )
            }
        }
    }

    val pendingReservationId = cashDialogFor
    if (pendingReservationId != null) {
        AlertDialog(
            onDismissRequest = { cashDialogFor = null },
            title = { Text(text = "Confirmar pago en efectivo") },
            text = {
                Text(
                    text = "Confirma que recibiste el pago en efectivo del estudiante antes de " +
                        "registrar su abordaje."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmCashPayment(pendingReservationId)
                        cashDialogFor = null
                    }
                ) {
                    Text(text = "Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { cashDialogFor = null }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }
}

@Composable
private fun PassengerCard(
    passenger: TripPassenger,
    onConfirmCash: () -> Unit,
    onRegisterBoarding: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = passenger.studentName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                StatusChip(
                    label = if (passenger.hasBoarded) "Abordó" else "Sin abordar",
                    tone = if (passenger.hasBoarded) StatusTone.POSITIVE else StatusTone.NEUTRAL
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusChip(
                    label = "Reserva: ${passenger.reservation.status.label}",
                    tone = passenger.reservation.status.tone()
                )
                StatusChip(
                    label = "Pago: ${passenger.payment.status.label}",
                    tone = passenger.payment.status.tone()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = passenger.payment.method.label,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = passenger.payment.amount.asPriceText(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (passenger.awaitsCashOnBoarding) {
                Button(
                    onClick = onConfirmCash,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Text(text = "Confirmar pago al abordar")
                }
            } else if (passenger.canRegisterBoarding) {
                OutlinedButton(
                    onClick = onRegisterBoarding,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Text(text = "Registrar abordaje")
                }
            }
        }
    }
}
