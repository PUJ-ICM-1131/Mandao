package com.uniruta.app.ui.screens.driver

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
import androidx.compose.material.icons.filled.Warning
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
import com.uniruta.app.data.model.ReservationStatus
import com.uniruta.app.data.model.TripPassenger
import com.uniruta.app.ui.components.InfoRow
import com.uniruta.app.ui.components.SectionTitle
import com.uniruta.app.ui.components.StatusChip
import com.uniruta.app.ui.components.UniRutaTopBar
import com.uniruta.app.ui.components.tone
import com.uniruta.app.viewmodel.ValidationOutcome

@Composable
fun BoardingResultScreen(
    outcome: ValidationOutcome,
    passenger: TripPassenger?,
    routeName: String,
    onRegisterBoarding: () -> Unit,
    onScanAgain: () -> Unit,
    onBackToTrip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val boarded = passenger?.reservation?.status == ReservationStatus.BOARDED
    val valid = outcome == ValidationOutcome.VALID

    val title = when {
        valid && boarded -> "Abordaje registrado"
        valid -> "Pase válido"
        outcome == ValidationOutcome.ALREADY_USED -> "Pase ya utilizado"
        outcome == ValidationOutcome.WRONG_TRIP -> "Pase de otro recorrido"
        outcome == ValidationOutcome.NOT_CONFIRMED -> "Reserva sin confirmar"
        else -> "Pase no válido"
    }

    val message = when {
        valid && boarded -> "El estudiante quedó registrado como abordado y su pase ya no puede volver a usarse."
        valid -> "Verifica los datos y registra el abordaje del estudiante."
        outcome == ValidationOutcome.ALREADY_USED -> "Este pase ya fue utilizado en este recorrido y no permite un nuevo abordaje."
        outcome == ValidationOutcome.WRONG_TRIP -> "Este pase no corresponde al recorrido actual."
        outcome == ValidationOutcome.NOT_CONFIRMED -> "La reserva asociada a este pase todavía no está confirmada."
        else -> "El código leído no corresponde a ningún pase emitido."
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { UniRutaTopBar(title = "Resultado de validación", onBack = onBackToTrip) }
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
                imageVector = if (valid) Icons.Default.CheckCircle else Icons.Default.Warning,
                contentDescription = null,
                tint = if (valid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                modifier = Modifier.size(56.dp)
            )

            Text(text = title, style = MaterialTheme.typography.headlineSmall)
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (passenger != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SectionTitle(text = "Datos del pasajero")
                        InfoRow(label = "Estudiante", value = passenger.studentName)
                        InfoRow(label = "Ruta", value = routeName)
                        InfoRow(label = "Reserva", value = passenger.reservation.id)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
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
                        if (passenger.pass != null) {
                            StatusChip(
                                label = "Pase: ${passenger.pass.status.label}",
                                tone = passenger.pass.status.tone(),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            if (valid && !boarded) {
                Button(
                    onClick = onRegisterBoarding,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Registrar abordaje")
                }
            }

            OutlinedButton(
                onClick = onScanAgain,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Validar otro pase")
            }

            OutlinedButton(
                onClick = onBackToTrip,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Volver al recorrido")
            }
        }
    }
}
