package com.uniruta.app.data.mock

import com.uniruta.app.data.model.DigitalPass
import com.uniruta.app.data.model.PassStatus
import com.uniruta.app.data.model.Payment
import com.uniruta.app.data.model.PaymentMethod
import com.uniruta.app.data.model.PaymentStatus
import com.uniruta.app.data.model.Reservation
import com.uniruta.app.data.model.ReservationStatus
import com.uniruta.app.data.model.Trip
import com.uniruta.app.data.model.TripPassenger
import java.time.LocalDateTime

object MockDriverData {

    const val DRIVER_NAME = "Carlos Medina"
    const val UNKNOWN_TOKEN = "00000000-0000-0000-0000-000000000000"

    private val anaReservation = MockStudentData.reservations.first { it.id == "res-2002" }
    private val anaPayment = MockStudentData.payments.first { it.reservationId == anaReservation.id }
    private val anaPass = MockStudentData.passes.first { it.reservationId == anaReservation.id }

    val demoToken: String = anaPass.token

    val assignedTrips: List<Trip> = MockTrips.all.filter { it.driverName == DRIVER_NAME }

    val passengers: List<TripPassenger> = listOf(
        TripPassenger(
            studentName = "Ana Gómez",
            reservation = anaReservation,
            payment = anaPayment,
            pass = anaPass
        ),
        TripPassenger(
            studentName = "Santiago Rojas",
            reservation = Reservation(
                id = "res-2101",
                tripId = "trip-001",
                reservationDate = LocalDateTime.now().minusDays(1).withHour(20).withMinute(10),
                status = ReservationStatus.CONFIRMED,
                selectedPaymentMethod = PaymentMethod.CASH_BOARDING
            ),
            payment = Payment(
                id = "pay-3101",
                reservationId = "res-2101",
                amount = 8500,
                method = PaymentMethod.CASH_BOARDING,
                status = PaymentStatus.PENDING
            )
        ),
        TripPassenger(
            studentName = "Valentina Cruz",
            reservation = Reservation(
                id = "res-2102",
                tripId = "trip-001",
                reservationDate = LocalDateTime.now().minusDays(2).withHour(8).withMinute(35),
                status = ReservationStatus.BOARDED,
                selectedPaymentMethod = PaymentMethod.TRANSFER
            ),
            payment = Payment(
                id = "pay-3102",
                reservationId = "res-2102",
                amount = 8500,
                method = PaymentMethod.TRANSFER,
                status = PaymentStatus.CONFIRMED,
                receiptName = "comprobante_transferencia.jpg"
            ),
            pass = DigitalPass(
                id = "pass-4102",
                reservationId = "res-2102",
                token = "c47d2a91-5b60-4f0e-9a72-3ed418c6b075",
                status = PassStatus.USED
            )
        ),
        TripPassenger(
            studentName = "Mateo Herrera",
            reservation = Reservation(
                id = "res-2103",
                tripId = "trip-005",
                reservationDate = LocalDateTime.now().minusDays(1).withHour(12).withMinute(5),
                status = ReservationStatus.CONFIRMED,
                selectedPaymentMethod = PaymentMethod.TRANSFER
            ),
            payment = Payment(
                id = "pay-3103",
                reservationId = "res-2103",
                amount = 8500,
                method = PaymentMethod.TRANSFER,
                status = PaymentStatus.CONFIRMED,
                receiptName = "comprobante_transferencia.jpg"
            ),
            pass = DigitalPass(
                id = "pass-4103",
                reservationId = "res-2103",
                token = "f10b73c4-2a88-4d31-bb05-7c9e2f4a6d12",
                status = PassStatus.ACTIVE
            )
        )
    )
}
