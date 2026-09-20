package com.uniruta.app.ui.screens.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.model.DigitalPass
import com.uniruta.app.data.model.Trip
import com.uniruta.app.ui.components.QrPlaceholder
import com.uniruta.app.ui.components.StatusChip
import com.uniruta.app.ui.components.UniRutaTopBar
import com.uniruta.app.ui.components.tone
import com.uniruta.app.ui.format.asLongText
import com.uniruta.app.ui.format.asText

@Composable
fun StudentPassScreen(
    pass: DigitalPass?,
    trip: Trip?,
    studentName: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { UniRutaTopBar(title = "Mi pase", onBack = onBack) }
    ) { innerPadding ->
        if (pass == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Todavía no tienes un pase disponible. Se genera cuando tu reserva y tu pago quedan confirmados.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "UniRuta",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        StatusChip(label = pass.status.label, tone = pass.status.tone())
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.25f)
                    )

                    PassField(label = "Ruta", value = trip?.route?.name ?: "—")
                    PassField(label = "Fecha", value = trip?.date?.asLongText() ?: "—")
                    PassField(label = "Hora de salida", value = trip?.departureTime?.asText() ?: "—")
                    PassField(label = "Estudiante", value = studentName)

                    QrPlaceholder(
                        token = pass.token,
                        modifier = Modifier
                            .fillMaxWidth(0.72f)
                            .padding(top = 20.dp)
                    )

                    Text(
                        text = pass.token,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }

            Text(
                text = "Presenta este pase al conductor al abordar.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp)
            )

            Text(
                text = "El código representa únicamente el identificador del pase.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
private fun PassField(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}
