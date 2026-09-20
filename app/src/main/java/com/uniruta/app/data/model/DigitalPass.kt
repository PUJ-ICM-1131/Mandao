package com.uniruta.app.data.model

enum class PassStatus(val label: String) {
    ACTIVE("Activo"),
    USED("Utilizado"),
    EXPIRED("Vencido")
}

data class DigitalPass(
    val id: String,
    val reservationId: String,
    val token: String,
    val status: PassStatus
)
