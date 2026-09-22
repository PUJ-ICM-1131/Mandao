package com.uniruta.app.ui.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.model.PaymentMethod
import com.uniruta.app.data.model.PaymentStatus
import com.uniruta.app.data.model.RejectionReason
import com.uniruta.app.data.model.Trip
import com.uniruta.app.data.model.TripPassenger
import com.uniruta.app.ui.components.InfoRow
import com.uniruta.app.ui.components.SectionTitle
import com.uniruta.app.ui.components.StatusChip
import com.uniruta.app.ui.components.UniRutaTopBar
import com.uniruta.app.ui.components.tone
import com.uniruta.app.ui.format.asLongText
import com.uniruta.app.ui.format.asPriceText
import com.uniruta.app.ui.format.asText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminPaymentDetailScreen(
    entry: TripPassenger,
    trip: Trip?,
    onApprove: () -> Unit,
    onReject: (RejectionReason) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var approveVisible by rememberSaveable { mutableStateOf(false) }
    var rejectVisible by rememberSaveable { mutableStateOf(false) }
    var selectedReason by rememberSaveable { mutableStateOf(RejectionReason.UNREADABLE_RECEIPT) }

    val payment = entry.payment
    val cashOnBoarding = payment.method == PaymentMethod.CASH_BOARDING
    val confirmed = payment.status == PaymentStatus.CONFIRMED
    val rejected = payment.status == PaymentStatus.REJECTED

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { UniRutaTopBar(title = "Detalle del pago", onBack = onBack) }
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
                    label = "Pago: ${payment.status.label}",
                    tone = payment.status.tone()
                )
                StatusChip(
                    label = "Reserva: ${entry.reservation.status.label}",
                    tone = entry.reservation.status.tone()
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionTitle(text = "Información")
                    InfoRow(label = "Estudiante", value = entry.studentName)
                    InfoRow(label = "Reserva", value = entry.reservation.id)
                    InfoRow(label = "Ruta", value = trip?.route?.name ?: "No disponible")
                    if (trip != null) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            InfoRow(
                                label = "Fecha",
                                value = trip.date.asLongText(),
                                modifier = Modifier.weight(1.3f)
                            )
                            InfoRow(
                                label = "Hora",
                                value = trip.departureTime.asText(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        InfoRow(
                            label = "Monto",
                            value = payment.amount.asPriceText(),
                            modifier = Modifier.weight(1f)
                        )
                        InfoRow(
                            label = "Método",
                            value = payment.method.label,
                            modifier = Modifier.weight(1.3f)
                        )
                    }
                    if (payment.rejectionReason != null) {
                        InfoRow(label = "Motivo del rechazo", value = payment.rejectionReason.label)
                    }
                }
            }

            if (payment.receiptName != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SectionTitle(text = "Comprobante")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null
                            )
                            Text(
                                text = payment.receiptName,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }
                }
            }

            if (entry.pass != null) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Pase emitido",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = entry.pass.token,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            when {
                cashOnBoarding -> {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Este pago se confirma durante el abordaje.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }

                confirmed -> {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Pago confirmado",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = "La reserva fue confirmada y el pase ya está disponible.",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                else -> {
                    Column {
                        if (rejected) {
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            ) {
                                Text(
                                    text = "Pago rechazado",
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        }
                        Button(
                            onClick = { approveVisible = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (payment.method == PaymentMethod.CASH_POINT) {
                                    "Confirmar pago recibido"
                                } else {
                                    "Aprobar pago"
                                }
                            )
                        }
                        if (payment.method == PaymentMethod.TRANSFER && !rejected) {
                            OutlinedButton(
                                onClick = { rejectVisible = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Text(text = "Rechazar pago")
                            }
                        }
                    }
                }
            }
        }
    }

    if (approveVisible) {
        AlertDialog(
            onDismissRequest = { approveVisible = false },
            title = { Text(text = "¿Aprobar el pago?") },
            text = {
                Text(
                    text = "La reserva quedará confirmada y se emitirá el pase digital del estudiante."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        approveVisible = false
                        onApprove()
                    }
                ) {
                    Text(text = "Aprobar")
                }
            },
            dismissButton = {
                TextButton(onClick = { approveVisible = false }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }

    if (rejectVisible) {
        AlertDialog(
            onDismissRequest = { rejectVisible = false },
            title = { Text(text = "¿Rechazar el pago?") },
            text = {
                Column {
                    Text(text = "La reserva permanecerá pendiente y no se emitirá pase.")
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        RejectionReason.entries.forEach { reason ->
                            FilterChip(
                                selected = selectedReason == reason,
                                onClick = { selectedReason = reason },
                                label = { Text(text = reason.label) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        rejectVisible = false
                        onReject(selectedReason)
                    }
                ) {
                    Text(text = "Rechazar")
                }
            },
            dismissButton = {
                TextButton(onClick = { rejectVisible = false }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }
}
