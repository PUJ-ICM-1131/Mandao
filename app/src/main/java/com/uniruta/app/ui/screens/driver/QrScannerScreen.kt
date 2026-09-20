package com.uniruta.app.ui.screens.driver

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uniruta.app.ui.components.QrScannerFrame
import com.uniruta.app.ui.components.StatusChip
import com.uniruta.app.ui.components.StatusTone
import com.uniruta.app.ui.components.UniRutaTopBar

@Composable
fun QrScannerScreen(
    routeName: String?,
    onSimulateValidRead: () -> Unit,
    onSimulateInvalidRead: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { UniRutaTopBar(title = "Validar abordaje", onBack = onBack) }
    ) { innerPadding ->
        if (routeName == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Para validar abordajes primero debes iniciar un recorrido desde " +
                        "tus recorridos asignados.",
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
                .padding(innerPadding)
                .padding(horizontal = 28.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = routeName,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            StatusChip(
                label = "Modo demostración",
                tone = StatusTone.NEUTRAL,
                modifier = Modifier.padding(top = 10.dp, bottom = 28.dp)
            )

            QrScannerFrame(modifier = Modifier.fillMaxWidth(0.78f))

            Text(
                text = "Ubica el pase QR del estudiante dentro del recuadro.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 28.dp)
            )

            Button(
                onClick = onSimulateValidRead,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp)
            ) {
                Text(text = "Simular lectura")
            }

            TextButton(
                onClick = onSimulateInvalidRead,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = "Probar pase inválido",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
