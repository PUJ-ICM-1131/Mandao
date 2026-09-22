package com.uniruta.app.data.model

data class TripPassenger(
    val studentName: String,
    val reservation: Reservation,
    val payment: Payment,
    val pass: DigitalPass? = null
) {
    val tripId: String
        get() = reservation.tripId

    val hasBoarded: Boolean
        get() = reservation.status == ReservationStatus.BOARDED

    val awaitsCashOnBoarding: Boolean
        get() = payment.method == PaymentMethod.CASH_BOARDING && payment.status == PaymentStatus.PENDING

    val canRegisterBoarding: Boolean
        get() = !hasBoarded &&
            reservation.status == ReservationStatus.CONFIRMED &&
            payment.status == PaymentStatus.CONFIRMED

    val reviewableByAdmin: Boolean
        get() = payment.method != PaymentMethod.CASH_BOARDING && reservation.isActive

    val requiresAdminReview: Boolean
        get() = reviewableByAdmin && (
            payment.status == PaymentStatus.UNDER_REVIEW ||
                (payment.method == PaymentMethod.CASH_POINT && payment.status == PaymentStatus.PENDING)
            )
}
