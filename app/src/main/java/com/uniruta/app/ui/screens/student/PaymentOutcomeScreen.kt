package com.uniruta.app.ui.screens.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.model.Payment
import com.uniruta.app.data.model.PaymentMethod
import com.uniruta.app.data.model.Reservation
import com.uniruta.app.ui.components.InfoRow
import com.uniruta.app.ui.components.SectionTitle
import com.uniruta.app.ui.components.StatusChip
import com.uniruta.app.ui.components.UniRutaTopBar
import com.uniruta.app.ui.components.tone
import com.uniruta.app.ui.format.asPriceText

@Composable
fun PaymentOutcomeScreen(
    reservation: Reservation,
    payment: Payment,
    routeName: String,
    onOpenReservations: () -> Unit,
    onBackHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title: String
    val message: String
    when (payment.method) {
        PaymentMethod.TRANSFER -> {
            title = "Comprobante enviado"
            message = "Tu pago está pendiente de validación."
        }
        PaymentMethod.CASH_POINT -> {
            title = "Reserva registrada"
            message = "Realiza el pago en un punto habilitado. El estado cambiará cuando el pago sea confirmado."
        }
        PaymentMethod.CASH_BOARDING -> {
            title = "Reserva registrada"
            message = "El pago será confirmado por el conductor al momento de abordar."
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { UniRutaTopBar(title = "Estado de la reserva") }
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

            Text(text = title, style = MaterialTheme.typography.headlineSmall)
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionTitle(text = "Detalle")
                    InfoRow(label = "Recorrido", value = routeName)
                    InfoRow(label = "Reserva", value = reservation.id)
                    InfoRow(label = "Método de pago", value = payment.method.label)
                    InfoRow(label = "Monto", value = payment.amount.asPriceText())
                    if (payment.receiptName != null) {
                        InfoRow(label = "Comprobante", value = payment.receiptName)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatusChip(
                            label = "Reserva: ${reservation.status.label}",
                            tone = reservation.status.tone()
                        )
                        StatusChip(
                            label = "Pago: ${payment.status.label}",
                            tone = payment.status.tone()
                        )
                    }
                }
            }

            Button(
                onClick = onOpenReservations,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Ver mis reservas")
            }

            OutlinedButton(
                onClick = onBackHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Volver al inicio")
            }
        }
    }
}
