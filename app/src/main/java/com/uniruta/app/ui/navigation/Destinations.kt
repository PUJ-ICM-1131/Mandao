package com.uniruta.app.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.uniruta.app.data.model.UserRole
import kotlinx.serialization.Serializable

@Serializable
data object Login : NavKey

@Serializable
data object StudentHome : NavKey

@Serializable
data object DriverHome : NavKey

@Serializable
data object AdminHome : NavKey

@Serializable
data object StudentTrips : NavKey

@Serializable
data class TripDetail(val tripId: String) : NavKey

@Serializable
data class ConfirmReservation(val tripId: String) : NavKey

@Serializable
data class PaymentMethodChoice(val reservationId: String) : NavKey

@Serializable
data class TransferPayment(val reservationId: String) : NavKey

@Serializable
data class PaymentOutcome(val reservationId: String) : NavKey

@Serializable
data object StudentReservations : NavKey

@Serializable
data class ReservationDetail(val reservationId: String) : NavKey

@Serializable
data class StudentPass(val reservationId: String? = null) : NavKey

fun homeDestinationFor(role: UserRole): NavKey = when (role) {
    UserRole.STUDENT -> StudentHome
    UserRole.DRIVER -> DriverHome
    UserRole.ADMIN -> AdminHome
}
