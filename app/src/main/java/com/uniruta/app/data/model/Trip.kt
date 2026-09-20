package com.uniruta.app.data.model

import java.time.LocalDate
import java.time.LocalTime

enum class TripStatus(val label: String) {
    SCHEDULED("Programado"),
    IN_PROGRESS("En curso"),
    COMPLETED("Finalizado"),
    CANCELLED("Cancelado")
}

data class Trip(
    val id: String,
    val route: Route,
    val date: LocalDate,
    val departureTime: LocalTime,
    val availableSeats: Int,
    val totalSeats: Int,
    val vehiclePlate: String,
    val driverName: String,
    val estimatedDurationMinutes: Int,
    val estimatedDistanceKm: Double,
    val price: Int,
    val status: TripStatus = TripStatus.SCHEDULED
) {
    val isBookable: Boolean
        get() = status == TripStatus.SCHEDULED && availableSeats > 0
}
