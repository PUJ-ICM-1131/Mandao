package com.uniruta.app.ui.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.model.Route
import com.uniruta.app.ui.components.SectionTitle
import com.uniruta.app.ui.components.UniRutaTopBar

@Composable
fun AdminRouteFormScreen(
    route: Route?,
    onSave: (String, String, String, List<String>) -> String?,
    onDeactivate: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable(route?.id) { mutableStateOf(route?.name.orEmpty()) }
    var origin by rememberSaveable(route?.id) { mutableStateOf(route?.origin.orEmpty()) }
    var destination by rememberSaveable(route?.id) { mutableStateOf(route?.destination.orEmpty()) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var deactivateVisible by rememberSaveable { mutableStateOf(false) }

    val stops = remember(route?.id) {
        mutableStateListOf<String>().apply {
            addAll(route?.stops?.sortedBy { it.order }?.map { it.name } ?: listOf("", ""))
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            UniRutaTopBar(
                title = if (route == null) "Nueva ruta" else "Editar ruta",
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Nombre de la ruta") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = origin,
                onValueChange = { origin = it },
                label = { Text(text = "Origen") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text(text = "Destino") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionTitle(text = "Paradas")

                    stops.forEachIndexed { index, stopName ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = stopName,
                                onValueChange = { stops[index] = it },
                                label = { Text(text = "Parada ${index + 1}") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    val previous = stops[index - 1]
                                    stops[index - 1] = stops[index]
                                    stops[index] = previous
                                },
                                enabled = index > 0
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Subir parada"
                                )
                            }
                            IconButton(
                                onClick = {
                                    val next = stops[index + 1]
                                    stops[index + 1] = stops[index]
                                    stops[index] = next
                                },
                                enabled = index < stops.lastIndex
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Bajar parada"
                                )
                            }
                            IconButton(
                                onClick = { stops.removeAt(index) },
                                enabled = stops.size > 2
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Eliminar parada"
                                )
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = { stops.add("") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Text(
                            text = "Agregar parada",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            val currentError = errorMessage
            if (currentError != null) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = currentError,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
                    )
                }
            }

            Button(
                onClick = {
                    val result = onSave(name, origin, destination, stops.toList())
                    errorMessage = result
                    if (result == null) onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Guardar ruta")
            }

            if (route != null && route.active) {
                OutlinedButton(
                    onClick = { deactivateVisible = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Desactivar ruta")
                }
            }
        }
    }

    if (deactivateVisible) {
        AlertDialog(
            onDismissRequest = { deactivateVisible = false },
            title = { Text(text = "¿Desactivar ruta?") },
            text = {
                Text(
                    text = "La ruta dejará de estar disponible para programar nuevos recorridos. " +
                        "Los recorridos ya programados no se modifican."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        deactivateVisible = false
                        onDeactivate()
                        onBack()
                    }
                ) {
                    Text(text = "Desactivar")
                }
            },
            dismissButton = {
                TextButton(onClick = { deactivateVisible = false }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }
}
