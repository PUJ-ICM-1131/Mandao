package com.uniruta.app.viewmodel

import androidx.lifecycle.ViewModel
import com.uniruta.app.data.mock.MockDriverData
import com.uniruta.app.data.model.AnomalyEvent
import com.uniruta.app.data.model.AnomalyType
import com.uniruta.app.data.model.Incident
import com.uniruta.app.data.model.IncidentType
import com.uniruta.app.data.model.PassStatus
import com.uniruta.app.data.model.PaymentStatus
import com.uniruta.app.data.model.ReservationStatus
import com.uniruta.app.data.model.TrackingStatus
import com.uniruta.app.data.model.Trip
import com.uniruta.app.data.model.TripPassenger
import com.uniruta.app.data.model.TripStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalTime

enum class ValidationOutcome {
    VALID,
    NOT_FOUND,
    WRONG_TRIP,
    NOT_CONFIRMED,
    ALREADY_USED
}

data class ValidationResult(
    val outcome: ValidationOutcome,
    val reservationId: String? = null
)

data class DriverUiState(
    val trips: List<Trip> = emptyList(),
    val passengers: List<TripPassenger> = emptyList(),
    val activeTripId: String? = null,
    val gpsStatus: TrackingStatus = TrackingStatus.INACTIVE,
    val sensorStatus: TrackingStatus = TrackingStatus.INACTIVE,
    val anomalies: List<AnomalyEvent> = emptyList(),
    val incidents: List<Incident> = emptyList(),
    val lastValidation: ValidationResult? = null
) {
    val activeTrip: Trip?
        get() = activeTripId?.let(::tripBy)

    val startableTrip: Trip?
        get() = trips.firstOrNull { it.status == TripStatus.SCHEDULED }

    fun tripBy(id: String): Trip? = trips.firstOrNull { it.id == id }

    fun passengersFor(tripId: String): List<TripPassenger> =
        passengers.filter { it.tripId == tripId && it.reservation.isActive }

    fun passengerBy(reservationId: String?): TripPassenger? =
        passengers.firstOrNull { it.reservation.id == reservationId }

    fun anomaliesFor(tripId: String): List<AnomalyEvent> =
        anomalies.filter { it.tripId == tripId }

    fun incidentsFor(tripId: String): List<Incident> =
        incidents.filter { it.tripId == tripId }

    fun boardedCountFor(tripId: String): Int =
        passengersFor(tripId).count { it.hasBoarded }
}

class DriverViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(initialState())
    val uiState: StateFlow<DriverUiState> = _uiState.asStateFlow()

    private var anomalySequence = 0
    private var incidentSequence = 0

    fun startTrip(tripId: String) {
        val state = _uiState.value
        if (state.activeTripId != null) return
        val trip = state.tripBy(tripId) ?: return
        if (trip.status != TripStatus.SCHEDULED) return

        _uiState.update { current ->
            current.copy(
                trips = current.trips.map {
                    if (it.id == tripId) it.copy(status = TripStatus.IN_PROGRESS) else it
                },
                activeTripId = tripId,
                gpsStatus = TrackingStatus.ACTIVE,
                sensorStatus = TrackingStatus.ACTIVE,
                anomalies = current.anomalies + startupAnomalies(trip)
            )
        }
    }

    fun finishTrip() {
        val tripId = _uiState.value.activeTripId ?: return
        _uiState.update { current ->
            current.copy(
                trips = current.trips.map {
                    if (it.id == tripId) it.copy(status = TripStatus.COMPLETED) else it
                },
                activeTripId = null,
                gpsStatus = TrackingStatus.INACTIVE,
                sensorStatus = TrackingStatus.INACTIVE,
                lastValidation = null
            )
        }
    }

    fun validateToken(token: String) {
        val state = _uiState.value
        val passenger = state.passengers.firstOrNull { it.pass?.token == token }
        val pass = passenger?.pass

        val result = when {
            passenger == null || pass == null -> ValidationResult(ValidationOutcome.NOT_FOUND)
            passenger.tripId != state.activeTripId ->
                ValidationResult(ValidationOutcome.WRONG_TRIP, passenger.reservation.id)
            pass.status != PassStatus.ACTIVE ->
                ValidationResult(ValidationOutcome.ALREADY_USED, passenger.reservation.id)
            passenger.reservation.status != ReservationStatus.CONFIRMED ->
                ValidationResult(ValidationOutcome.NOT_CONFIRMED, passenger.reservation.id)
            else -> ValidationResult(ValidationOutcome.VALID, passenger.reservation.id)
        }

        _uiState.update { it.copy(lastValidation = result) }
    }

    fun clearValidation() {
        _uiState.update { it.copy(lastValidation = null) }
    }

    fun registerBoarding(reservationId: String) {
        val passenger = _uiState.value.passengerBy(reservationId) ?: return
        if (!passenger.canRegisterBoarding) return

        _uiState.update { current ->
            current.copy(
                passengers = current.passengers.map { item ->
                    if (item.reservation.id != reservationId) {
                        item
                    } else {
                        item.copy(
                            reservation = item.reservation.copy(status = ReservationStatus.BOARDED),
                            pass = item.pass?.copy(status = PassStatus.USED)
                        )
                    }
                }
            )
        }
    }

    fun confirmCashPayment(reservationId: String) {
        _uiState.update { current ->
            current.copy(
                passengers = current.passengers.map { item ->
                    if (item.reservation.id != reservationId || !item.awaitsCashOnBoarding) {
                        item
                    } else {
                        item.copy(payment = item.payment.copy(status = PaymentStatus.CONFIRMED))
                    }
                }
            )
        }
    }

    fun reportIncident(type: IncidentType, description: String, evidenceName: String?) {
        val tripId = _uiState.value.activeTripId ?: return
        val incident = Incident(
            id = "inc-${++incidentSequence + 5000}",
            tripId = tripId,
            type = type,
            description = description.trim(),
            evidenceName = evidenceName,
            reportedAt = LocalTime.now()
        )
        _uiState.update { it.copy(incidents = it.incidents + incident) }
    }

    fun reset() {
        anomalySequence = 0
        incidentSequence = 0
        _uiState.value = initialState()
    }

    private fun startupAnomalies(trip: Trip): List<AnomalyEvent> = listOf(
        AnomalyEvent(
            id = "anm-${++anomalySequence + 7000}",
            tripId = trip.id,
            type = AnomalyType.HARD_BRAKING,
            time = trip.departureTime.plusMinutes(8)
        ),
        AnomalyEvent(
            id = "anm-${++anomalySequence + 7000}",
            tripId = trip.id,
            type = AnomalyType.SHARP_TURN,
            time = trip.departureTime.plusMinutes(15)
        )
    )

    private fun initialState() = DriverUiState(
        trips = MockDriverData.assignedTrips,
        passengers = MockDriverData.passengers
    )
}
