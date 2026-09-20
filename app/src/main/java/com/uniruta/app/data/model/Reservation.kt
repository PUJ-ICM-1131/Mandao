package com.uniruta.app.data.model

import java.time.LocalDateTime

enum class ReservationStatus(val label: String) {
    PENDING("Pendiente"),
    CONFIRMED("Confirmada"),
    BOARDED("Abordada"),
    COMPLETED("Completada"),
    CANCELLED("Cancelada")
}

data class Reservation(
    val id: String,
    val tripId: String,
    val reservationDate: LocalDateTime,
    val status: ReservationStatus,
    val selectedPaymentMethod: PaymentMethod? = null
) {
    val isActive: Boolean
        get() = status != ReservationStatus.CANCELLED
}
