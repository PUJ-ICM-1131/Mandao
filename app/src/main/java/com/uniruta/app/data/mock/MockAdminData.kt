package com.uniruta.app.data.mock

import com.uniruta.app.data.model.Payment
import com.uniruta.app.data.model.PaymentMethod
import com.uniruta.app.data.model.PaymentStatus
import com.uniruta.app.data.model.Reservation
import com.uniruta.app.data.model.ReservationStatus
import com.uniruta.app.data.model.Route
import com.uniruta.app.data.model.Trip
import com.uniruta.app.data.model.TripPassenger
import com.uniruta.app.data.model.Vehicle
import com.uniruta.app.data.model.VehicleStatus
import java.time.LocalDateTime

object MockAdminData {

    val routes: List<Route> = MockTrips.all.map { it.route }.distinctBy { it.id }

    val trips: List<Trip> = MockTrips.all

    val drivers: List<String> = listOf(
        "Carlos Medina",
        "Marcela Ríos",
        "Andrés Lozano",
        "Diana Castro"
    )

    val vehicles: List<Vehicle> = listOf(
        Vehicle("veh-001", "TUV-421", 25, VehicleStatus.ACTIVE),
        Vehicle("veh-002", "WXY-118", 20, VehicleStatus.ACTIVE),
        Vehicle("veh-003", "GHT-905", 22, VehicleStatus.MAINTENANCE),
        Vehicle("veh-004", "KLP-337", 25, VehicleStatus.ACTIVE)
    )

    private const val STUDENT_NAME = "Ana Gómez"

    private fun studentEntry(reservationId: String): TripPassenger {
        val reservation = MockStudentData.reservations.first { it.id == reservationId }
        return TripPassenger(
            studentName = STUDENT_NAME,
            reservation = reservation,
            payment = MockStudentData.payments.first { it.reservationId == reservationId },
            pass = MockStudentData.passes.firstOrNull { it.reservationId == reservationId }
        )
    }

    private val cashPointEntry = TripPassenger(
        studentName = "Mariana Pardo",
        reservation = Reservation(
            id = "res-2201",
            tripId = "trip-002",
            reservationDate = LocalDateTime.now().minusHours(6),
            status = ReservationStatus.PENDING,
            selectedPaymentMethod = PaymentMethod.CASH_POINT
        ),
        payment = Payment(
            id = "pay-3201",
            reservationId = "res-2201",
            amount = 5500,
            method = PaymentMethod.CASH_POINT,
            status = PaymentStatus.PENDING
        )
    )

    val passengers: List<TripPassenger> = listOf(
        studentEntry("res-2001"),
        cashPointEntry,
        MockDriverData.passengers.first { it.reservation.id == "res-2101" },
        studentEntry("res-2002"),
        studentEntry("res-2003")
    )
}
