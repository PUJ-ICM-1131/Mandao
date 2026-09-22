package com.uniruta.app.ui.screens.admin

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.model.Route
import com.uniruta.app.data.model.Vehicle
import com.uniruta.app.ui.components.SectionTitle
import com.uniruta.app.ui.components.UniRutaTopBar
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
private val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminTripFormScreen(
    routes: List<Route>,
    vehicles: List<Vehicle>,
    drivers: List<String>,
    onSchedule: (String?, LocalDate?, LocalTime?, String?, String?, String, String) -> String?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var routeId by rememberSaveable { mutableStateOf(routes.firstOrNull()?.id) }
    var vehicleId by rememberSaveable { mutableStateOf(vehicles.firstOrNull()?.id) }
    var driverName by rememberSaveable { mutableStateOf(drivers.firstOrNull()) }
    var dateText by rememberSaveable { mutableStateOf(LocalDate.now().plusDays(1).format(dateFormat)) }
    var timeText by rememberSaveable { mutableStateOf("06:30") }
    var seats by rememberSaveable { mutableStateOf("20") }
    var price by rememberSaveable { mutableStateOf("6000") }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var scheduled by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { UniRutaTopBar(title = "Programar recorrido", onBack = onBack) }
    ) { innerPadding ->
        if (scheduled) {
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
                    text = "Recorrido programado",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Ya aparece en la lista de recorridos.",
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
                    Text(text = "Volver a recorridos")
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column {
                SectionTitle(text = "Ruta activa")
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    routes.forEach { route ->
                        FilterChip(
                            selected = routeId == route.id,
                            onClick = { routeId = route.id },
                            label = { Text(text = route.name) }
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = dateText,
                    onValueChange = { dateText = it },
                    label = { Text(text = "Fecha") },
                    placeholder = { Text(text = "dd/MM/aaaa") },
                    singleLine = true,
                    modifier = Modifier.weight(1.3f)
                )
                OutlinedTextField(
                    value = timeText,
                    onValueChange = { timeText = it },
                    label = { Text(text = "Hora") },
                    placeholder = { Text(text = "HH:mm") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Column {
                SectionTitle(text = "Vehículo activo")
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    vehicles.forEach { vehicle ->
                        FilterChip(
                            selected = vehicleId == vehicle.id,
                            onClick = { vehicleId = vehicle.id },
                            label = { Text(text = "${vehicle.plate} · ${vehicle.capacity}") }
                        )
                    }
                }
            }

            Column {
                SectionTitle(text = "Conductor")
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    drivers.forEach { driver ->
                        FilterChip(
                            selected = driverName == driver,
                            onClick = { driverName = driver },
                            label = { Text(text = driver) }
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = seats,
                    onValueChange = { seats = it },
                    label = { Text(text = "Cupos") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text(text = "Precio") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
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
                    val result = onSchedule(
                        routeId,
                        parseDate(dateText),
                        parseTime(timeText),
                        vehicleId,
                        driverName,
                        seats,
                        price
                    )
                    errorMessage = result
                    if (result == null) scheduled = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Programar recorrido")
            }
        }
    }
}

private fun parseDate(value: String): LocalDate? = runCatching {
    LocalDate.parse(value.trim(), dateFormat)
}.getOrNull()

private fun parseTime(value: String): LocalTime? = runCatching {
    LocalTime.parse(value.trim(), timeFormat)
}.getOrNull()
