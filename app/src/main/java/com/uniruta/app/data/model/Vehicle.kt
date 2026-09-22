package com.uniruta.app.data.model

enum class VehicleStatus(val label: String) {
    ACTIVE("Activo"),
    INACTIVE("Inactivo"),
    MAINTENANCE("Mantenimiento")
}

data class Vehicle(
    val id: String,
    val plate: String,
    val capacity: Int,
    val status: VehicleStatus
)
