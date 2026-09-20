package com.uniruta.app.viewmodel

import androidx.lifecycle.ViewModel
import com.uniruta.app.data.mock.MockStudentData
import com.uniruta.app.data.mock.MockTrips
import com.uniruta.app.data.model.DigitalPass
import com.uniruta.app.data.model.PassStatus
import com.uniruta.app.data.model.Payment
import com.uniruta.app.data.model.PaymentMethod
import com.uniruta.app.data.model.PaymentStatus
import com.uniruta.app.data.model.Reservation
import com.uniruta.app.data.model.ReservationStatus
import com.uniruta.app.data.model.Trip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime

private const val MOCK_RECEIPT_NAME = "comprobante_transferencia.jpg"

data class StudentUiState(
    val trips: List<Trip> = emptyList(),
    val reservations: List<Reservation> = emptyList(),
    val payments: List<Payment> = emptyList(),
    val passes: List<DigitalPass> = emptyList()
) {
    val orderedReservations: List<Reservation>
        get() = reservations.sortedByDescending { it.reservationDate }

    fun tripBy(id: String): Trip? = trips.firstOrNull { it.id == id }

    fun reservationBy(id: String): Reservation? = reservations.firstOrNull { it.id == id }

    fun paymentFor(reservationId: String): Payment? =
        payments.firstOrNull { it.reservationId == reservationId }

    fun hasActiveReservationFor(tripId: String): Boolean =
        reservations.any { it.tripId == tripId && it.isActive }

    fun availablePassFor(reservationId: String?): DigitalPass? {
        val candidates = if (reservationId == null) passes else passes.filter { it.reservationId == reservationId }
        return candidates.firstOrNull { pass ->
            pass.status == PassStatus.ACTIVE &&
                reservationBy(pass.reservationId)?.status == ReservationStatus.CONFIRMED &&
                paymentFor(pass.reservationId)?.status == PaymentStatus.CONFIRMED
        }
    }
}

class StudentViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(initialState())
    val uiState: StateFlow<StudentUiState> = _uiState.asStateFlow()

    private var reservationSequence = 0
    private var paymentSequence = 0

    fun reserveSeat(tripId: String): String? {
        val state = _uiState.value
        state.reservations.firstOrNull { it.tripId == tripId && it.isActive }?.let { return it.id }

        val trip = state.tripBy(tripId) ?: return null
        if (!trip.isBookable) return null

        val reservation = Reservation(
            id = "res-${++reservationSequence + 9000}",
            tripId = tripId,
            reservationDate = LocalDateTime.now(),
            status = ReservationStatus.PENDING
        )

        _uiState.update { current ->
            current.copy(
                trips = current.trips.map {
                    if (it.id == tripId) it.copy(availableSeats = it.availableSeats - 1) else it
                },
                reservations = current.reservations + reservation
            )
        }
        return reservation.id
    }

    fun selectPaymentMethod(reservationId: String, method: PaymentMethod) {
        val state = _uiState.value
        val reservation = state.reservationBy(reservationId) ?: return
        val amount = state.tripBy(reservation.tripId)?.price ?: return
        val existing = state.paymentFor(reservationId)

        val payment = existing?.copy(
            method = method,
            amount = amount,
            status = PaymentStatus.PENDING,
            receiptName = null
        ) ?: Payment(
            id = "pay-${++paymentSequence + 9000}",
            reservationId = reservationId,
            amount = amount,
            method = method,
            status = PaymentStatus.PENDING
        )

        _uiState.update { current ->
            current.copy(
                reservations = current.reservations.map {
                    if (it.id == reservationId) it.copy(selectedPaymentMethod = method) else it
                },
                payments = current.payments.filterNot { it.reservationId == reservationId } + payment
            )
        }
    }

    fun attachReceipt(reservationId: String) {
        updatePayment(reservationId) { it.copy(receiptName = MOCK_RECEIPT_NAME) }
    }

    fun submitReceipt(reservationId: String) {
        updatePayment(reservationId) { payment ->
            if (payment.receiptName == null) payment else payment.copy(status = PaymentStatus.UNDER_REVIEW)
        }
    }

    fun cancelReservation(reservationId: String) {
        val reservation = _uiState.value.reservationBy(reservationId) ?: return
        if (!reservation.isActive) return

        _uiState.update { current ->
            current.copy(
                trips = current.trips.map {
                    if (it.id == reservation.tripId) it.copy(availableSeats = it.availableSeats + 1) else it
                },
                reservations = current.reservations.map {
                    if (it.id == reservationId) it.copy(status = ReservationStatus.CANCELLED) else it
                }
            )
        }
    }

    fun reset() {
        reservationSequence = 0
        paymentSequence = 0
        _uiState.value = initialState()
    }

    private fun updatePayment(reservationId: String, transform: (Payment) -> Payment) {
        _uiState.update { current ->
            current.copy(
                payments = current.payments.map {
                    if (it.reservationId == reservationId) transform(it) else it
                }
            )
        }
    }

    private fun initialState() = StudentUiState(
        trips = MockTrips.all,
        reservations = MockStudentData.reservations,
        payments = MockStudentData.payments,
        passes = MockStudentData.passes
    )
}
