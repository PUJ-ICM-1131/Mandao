package com.uniruta.app.ui.screens.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniruta.app.ui.components.InfoRow
import com.uniruta.app.ui.components.SectionTitle
import com.uniruta.app.ui.components.UniRutaTopBar
import com.uniruta.app.ui.format.asPriceText

@Composable
fun TransferPaymentScreen(
    amount: Int,
    reservationId: String,
    receiptName: String?,
    onAttachReceipt: () -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { UniRutaTopBar(title = "Transferencia", onBack = onBack) }
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
                    SectionTitle(text = "Datos de la transferencia")
                    InfoRow(label = "Monto a transferir", value = amount.asPriceText())
                    InfoRow(label = "Referencia de la reserva", value = reservationId)
                }
            }

            Text(
                text = "Adjunta el comprobante de la transferencia para que sea revisado.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedButton(
                onClick = onAttachReceipt,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Seleccionar comprobante")
            }

            if (receiptName != null) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null
                        )
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(
                                text = receiptName,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Comprobante seleccionado",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }

            Button(
                onClick = onSubmit,
                enabled = receiptName != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Enviar comprobante")
            }
        }
    }
}
