package com.uniruta.app.data.model

import java.time.LocalTime

enum class SensorSource(val label: String) {
    ACCELEROMETER("Acelerómetro"),
    GYROSCOPE("Giroscopio")
}

enum class AnomalyType(val label: String, val source: SensorSource) {
    HARD_BRAKING("Frenada brusca", SensorSource.ACCELEROMETER),
    HARD_ACCELERATION("Aceleración brusca", SensorSource.ACCELEROMETER),
    SHARP_TURN("Giro pronunciado", SensorSource.GYROSCOPE)
}

data class AnomalyEvent(
    val id: String,
    val tripId: String,
    val type: AnomalyType,
    val time: LocalTime
)
