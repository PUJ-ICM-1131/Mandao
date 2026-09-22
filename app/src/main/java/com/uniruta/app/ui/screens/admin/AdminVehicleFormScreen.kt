package com.uniruta.app.ui.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.model.Vehicle
import com.uniruta.app.data.model.VehicleStatus
import com.uniruta.app.ui.components.SectionTitle
import com.uniruta.app.ui.components.UniRutaTopBar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminVehicleFormScreen(
    vehicle: Vehicle?,
    onSave: (String, String, VehicleStatus) -> String?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var plate by rememberSaveable(vehicle?.id) { mutableStateOf(vehicle?.plate.orEmpty()) }
    var capacity by rememberSaveable(vehicle?.id) {
        mutableStateOf(vehicle?.capacity?.toString().orEmpty())
    }
    var status by rememberSaveable(vehicle?.id) {
        mutableStateOf(vehicle?.status ?: VehicleStatus.ACTIVE)
    }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            UniRutaTopBar(
                title = if (vehicle == null) "Nuevo vehículo" else "Editar vehículo",
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
                value = plate,
                onValueChange = { plate = it },
                label = { Text(text = "Placa") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = capacity,
                onValueChange = { capacity = it },
                label = { Text(text = "Capacidad") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Column {
                SectionTitle(text = "Estado")
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    VehicleStatus.entries.forEach { option ->
                        FilterChip(
                            selected = status == option,
                            onClick = { status = option },
                            label = { Text(text = option.label) }
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
                    val result = onSave(plate, capacity, status)
                    errorMessage = result
                    if (result == null) onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Guardar vehículo")
            }
        }
    }
}
