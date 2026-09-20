package com.uniruta.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.uniruta.app.data.model.PaymentMethod
import com.uniruta.app.ui.screens.admin.AdminHomeScreen
import com.uniruta.app.ui.screens.auth.LoginScreen
import com.uniruta.app.ui.screens.driver.DriverHomeScreen
import com.uniruta.app.ui.screens.student.ConfirmReservationScreen
import com.uniruta.app.ui.screens.student.PaymentMethodScreen
import com.uniruta.app.ui.screens.student.PaymentOutcomeScreen
import com.uniruta.app.ui.screens.student.ReservationDetailScreen
import com.uniruta.app.ui.screens.student.ReservationRow
import com.uniruta.app.ui.screens.student.StudentHomeScreen
import com.uniruta.app.ui.screens.student.StudentPassScreen
import com.uniruta.app.ui.screens.student.StudentReservationsScreen
import com.uniruta.app.ui.screens.student.StudentTripsScreen
import com.uniruta.app.ui.screens.student.TransferPaymentScreen
import com.uniruta.app.ui.screens.student.TripDetailScreen
import com.uniruta.app.viewmodel.AuthViewModel
import com.uniruta.app.viewmodel.StudentUiState
import com.uniruta.app.viewmodel.StudentViewModel

@Composable
fun UniRutaApp(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(),
    studentViewModel: StudentViewModel = viewModel()
) {
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    val studentState by studentViewModel.uiState.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(Login)
    val currentUser = authState.currentUser

    LaunchedEffect(currentUser?.role) {
        if (currentUser == null) studentViewModel.reset()
        val destination = currentUser?.role?.let(::homeDestinationFor) ?: Login
        if (backStack.lastOrNull() != destination) {
            backStack.clear()
            backStack.add(destination)
        }
    }

    fun backToStudentHome() {
        backStack.clear()
        backStack.add(StudentHome)
    }

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Login> {
                LoginScreen(
                    state = authState,
                    onEmailChange = authViewModel::onEmailChange,
                    onPasswordChange = authViewModel::onPasswordChange,
                    onSubmit = authViewModel::login,
                    onDemoLogin = authViewModel::loginAs
                )
            }

            entry<StudentHome> {
                currentUser?.let { user ->
                    StudentHomeScreen(
                        user = user,
                        onOpenTrips = { backStack.add(StudentTrips) },
                        onOpenReservations = { backStack.add(StudentReservations) },
                        onOpenPass = { backStack.add(StudentPass()) },
                        onLogout = authViewModel::logout
                    )
                }
            }

            entry<StudentTrips> {
                StudentTripsScreen(
                    trips = studentState.trips,
                    onTripSelected = { backStack.add(TripDetail(it)) },
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<TripDetail> { key ->
                studentState.tripBy(key.tripId)?.let { trip ->
                    TripDetailScreen(
                        trip = trip,
                        alreadyReserved = studentState.hasActiveReservationFor(trip.id),
                        onReserve = { backStack.add(ConfirmReservation(trip.id)) },
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
            }

            entry<ConfirmReservation> { key ->
                studentState.tripBy(key.tripId)?.let { trip ->
                    ConfirmReservationScreen(
                        trip = trip,
                        onConfirm = {
                            studentViewModel.reserveSeat(trip.id)?.let { reservationId ->
                                backStack.add(PaymentMethodChoice(reservationId))
                            }
                        },
                        onCancel = { backStack.removeLastOrNull() },
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
            }

            entry<PaymentMethodChoice> { key ->
                val reservation = studentState.reservationBy(key.reservationId)
                val amount = reservation?.let { studentState.tripBy(it.tripId)?.price } ?: 0
                PaymentMethodScreen(
                    amount = amount,
                    onMethodSelected = { method ->
                        studentViewModel.selectPaymentMethod(key.reservationId, method)
                        if (method == PaymentMethod.TRANSFER) {
                            backStack.add(TransferPayment(key.reservationId))
                        } else {
                            backStack.add(PaymentOutcome(key.reservationId))
                        }
                    },
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<TransferPayment> { key ->
                val payment = studentState.paymentFor(key.reservationId)
                TransferPaymentScreen(
                    amount = payment?.amount ?: 0,
                    reservationId = key.reservationId,
                    receiptName = payment?.receiptName,
                    onAttachReceipt = { studentViewModel.attachReceipt(key.reservationId) },
                    onSubmit = {
                        studentViewModel.submitReceipt(key.reservationId)
                        backStack.add(PaymentOutcome(key.reservationId))
                    },
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<PaymentOutcome> { key ->
                val reservation = studentState.reservationBy(key.reservationId)
                val payment = studentState.paymentFor(key.reservationId)
                if (reservation != null && payment != null) {
                    PaymentOutcomeScreen(
                        reservation = reservation,
                        payment = payment,
                        routeName = studentState.tripBy(reservation.tripId)?.route?.name.orEmpty(),
                        onOpenReservations = {
                            backToStudentHome()
                            backStack.add(StudentReservations)
                        },
                        onBackHome = ::backToStudentHome
                    )
                }
            }

            entry<StudentReservations> {
                StudentReservationsScreen(
                    rows = studentState.reservationRows(),
                    onReservationSelected = { backStack.add(ReservationDetail(it)) },
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<ReservationDetail> { key ->
                studentState.reservationRow(key.reservationId)?.let { row ->
                    ReservationDetailScreen(
                        row = row,
                        passAvailable = studentState.availablePassFor(key.reservationId) != null,
                        onOpenPass = { backStack.add(StudentPass(key.reservationId)) },
                        onCancelReservation = {
                            studentViewModel.cancelReservation(key.reservationId)
                            backStack.removeLastOrNull()
                        },
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
            }

            entry<StudentPass> { key ->
                val pass = studentState.availablePassFor(key.reservationId)
                val trip = pass
                    ?.let { studentState.reservationBy(it.reservationId) }
                    ?.let { studentState.tripBy(it.tripId) }
                StudentPassScreen(
                    pass = pass,
                    trip = trip,
                    studentName = currentUser?.name.orEmpty(),
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<DriverHome> {
                currentUser?.let { DriverHomeScreen(user = it, onLogout = authViewModel::logout) }
            }

            entry<AdminHome> {
                currentUser?.let { AdminHomeScreen(user = it, onLogout = authViewModel::logout) }
            }
        }
    )
}

private fun StudentUiState.reservationRows(): List<ReservationRow> =
    orderedReservations.map { reservation ->
        ReservationRow(
            reservation = reservation,
            trip = tripBy(reservation.tripId),
            payment = paymentFor(reservation.id)
        )
    }

private fun StudentUiState.reservationRow(reservationId: String): ReservationRow? =
    reservationBy(reservationId)?.let { reservation ->
        ReservationRow(
            reservation = reservation,
            trip = tripBy(reservation.tripId),
            payment = paymentFor(reservation.id)
        )
    }
