package com.uniruta.app.data.mock

import com.uniruta.app.data.model.DigitalPass
import com.uniruta.app.data.model.PassStatus
import com.uniruta.app.data.model.Payment
import com.uniruta.app.data.model.PaymentMethod
import com.uniruta.app.data.model.PaymentStatus
import com.uniruta.app.data.model.Reservation
import com.uniruta.app.data.model.ReservationStatus
import java.time.LocalDateTime

object MockStudentData {

    private val now: LocalDateTime = LocalDateTime.now()

    val reservations: List<Reservation> = listOf(
        Reservation(
            id = "res-2001",
            tripId = "trip-005",
            reservationDate = now.minusDays(1).withHour(19).withMinute(42),
            status = ReservationStatus.PENDING,
            selectedPaymentMethod = PaymentMethod.TRANSFER
        ),
        Reservation(
            id = "res-2002",
            tripId = "trip-001",
            reservationDate = now.minusDays(2).withHour(9).withMinute(15),
            status = ReservationStatus.CONFIRMED,
            selectedPaymentMethod = PaymentMethod.TRANSFER
        ),
        Reservation(
            id = "res-2003",
            tripId = "trip-004",
            reservationDate = now.minusDays(4).withHour(17).withMinute(3),
            status = ReservationStatus.CANCELLED,
            selectedPaymentMethod = PaymentMethod.CASH_POINT
        )
    )

    val payments: List<Payment> = listOf(
        Payment(
            id = "pay-3001",
            reservationId = "res-2001",
            amount = 8500,
            method = PaymentMethod.TRANSFER,
            status = PaymentStatus.UNDER_REVIEW,
            receiptName = "comprobante_transferencia.jpg"
        ),
        Payment(
            id = "pay-3002",
            reservationId = "res-2002",
            amount = 8500,
            method = PaymentMethod.TRANSFER,
            status = PaymentStatus.CONFIRMED,
            receiptName = "comprobante_transferencia.jpg"
        ),
        Payment(
            id = "pay-3003",
            reservationId = "res-2003",
            amount = 6000,
            method = PaymentMethod.CASH_POINT,
            status = PaymentStatus.PENDING
        )
    )

    val passes: List<DigitalPass> = listOf(
        DigitalPass(
            id = "pass-4001",
            reservationId = "res-2002",
            token = "8b9f41f6-7c21-4c21-a3d8-19ba5d0c7e44",
            status = PassStatus.ACTIVE
        )
    )
}
