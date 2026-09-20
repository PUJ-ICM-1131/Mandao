package com.uniruta.app.ui.screens.driver

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.model.IncidentType
import com.uniruta.app.ui.components.SectionTitle
import com.uniruta.app.ui.components.UniRutaTopBar

private const val MOCK_EVIDENCE_NAME = "evidencia_recorrido.jpg"

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReportIncidentScreen(
    onSubmit: (IncidentType, String, String?) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedType by rememberSaveable { mutableStateOf(IncidentType.TRAFFIC) }
    var description by rememberSaveable { mutableStateOf("") }
    var evidenceName by rememberSaveable { mutableStateOf<String?>(null) }
    var submitted by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { UniRutaTopBar(title = "Reportar novedad", onBack = onBack) }
    ) { innerPadding ->
        if (submitted) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(56.dp)
                )
                Text(
                    text = "Novedad registrada",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Quedó asociada al recorrido en curso.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp)
                ) {
                    Text(text = "Volver al recorrido")
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                SectionTitle(text = "Tipo de novedad")
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IncidentType.entries.forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(text = type.label) }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(text = "Descripción") },
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Column {
                SectionTitle(text = "Evidencia fotográfica")
                OutlinedButton(
                    onClick = { evidenceName = MOCK_EVIDENCE_NAME },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Agregar fotografía")
                }

                if (evidenceName != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null
                            )
                            Text(
                                text = evidenceName.orEmpty(),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    onSubmit(selectedType, description, evidenceName)
                    submitted = true
                },
                enabled = description.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Registrar novedad")
            }
        }
    }
}
