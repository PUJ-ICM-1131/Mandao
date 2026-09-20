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
import com.uniruta.app.data.mock.MockDriverData
import com.uniruta.app.data.model.PaymentMethod
import com.uniruta.app.ui.screens.admin.AdminHomeScreen
import com.uniruta.app.ui.screens.auth.LoginScreen
import com.uniruta.app.ui.screens.driver.ActiveTripScreen
import com.uniruta.app.ui.screens.driver.BoardingResultScreen
import com.uniruta.app.ui.screens.driver.DriverHomeScreen
import com.uniruta.app.ui.screens.driver.DriverPassengersScreen
import com.uniruta.app.ui.screens.driver.DriverTripDetailScreen
import com.uniruta.app.ui.screens.driver.DriverTripsScreen
import com.uniruta.app.ui.screens.driver.QrScannerScreen
import com.uniruta.app.ui.screens.driver.ReportIncidentScreen
import com.uniruta.app.ui.screens.driver.TripFinishedScreen
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
import com.uniruta.app.viewmodel.DriverViewModel
import com.uniruta.app.viewmodel.StudentUiState
import com.uniruta.app.viewmodel.StudentViewModel

@Composable
fun UniRutaApp(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(),
    studentViewModel: StudentViewModel = viewModel(),
    driverViewModel: DriverViewModel = viewModel()
) {
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    val studentState by studentViewModel.uiState.collectAsStateWithLifecycle()
    val driverState by driverViewModel.uiState.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(Login)
    val currentUser = authState.currentUser

    LaunchedEffect(currentUser?.role) {
        if (currentUser == null) {
            studentViewModel.reset()
            driverViewModel.reset()
        }
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

    fun backToDriverHome() {
        backStack.clear()
        backStack.add(DriverHome)
    }

    fun openActiveTrip() {
        backStack.clear()
        backStack.add(DriverHome)
        backStack.add(ActiveTrip)
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
                currentUser?.let { user ->
                    DriverHomeScreen(
                        user = user,
                        hasActiveTrip = driverState.activeTrip != null,
                        onOpenTrips = { backStack.add(DriverTrips) },
                        onStartTrip = {
                            val startable = driverState.startableTrip
                            when {
                                driverState.activeTrip != null -> openActiveTrip()
                                startable != null -> backStack.add(DriverTripDetail(startable.id))
                                else -> backStack.add(DriverTrips)
                            }
                        },
                        onValidateBoarding = { backStack.add(QrScannerMock) },
                        onLogout = authViewModel::logout
                    )
                }
            }

            entry<DriverTrips> {
                DriverTripsScreen(
                    trips = driverState.trips,
                    driverName = currentUser?.name.orEmpty(),
                    onTripSelected = { backStack.add(DriverTripDetail(it)) },
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<DriverTripDetail> { key ->
                driverState.tripBy(key.tripId)?.let { trip ->
                    DriverTripDetailScreen(
                        trip = trip,
                        passengerCount = driverState.passengersFor(trip.id).size,
                        startBlockedReason = if (driverState.activeTripId != null) {
                            "Ya tienes un recorrido en curso. Finalízalo antes de iniciar otro."
                        } else {
                            null
                        },
                        onStartTrip = {
                            driverViewModel.startTrip(trip.id)
                            openActiveTrip()
                        },
                        onOpenActiveTrip = ::openActiveTrip,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
            }

            entry<ActiveTrip> {
                driverState.activeTrip?.let { trip ->
                    ActiveTripScreen(
                        trip = trip,
                        gpsStatus = driverState.gpsStatus,
                        sensorStatus = driverState.sensorStatus,
                        boardedCount = driverState.boardedCountFor(trip.id),
                        passengerCount = driverState.passengersFor(trip.id).size,
                        anomalies = driverState.anomaliesFor(trip.id),
                        incidentCount = driverState.incidentsFor(trip.id).size,
                        onValidateBoarding = { backStack.add(QrScannerMock) },
                        onOpenPassengers = { backStack.add(DriverPassengers) },
                        onReportIncident = { backStack.add(ReportIncident) },
                        onFinishTrip = {
                            val finishedTripId = trip.id
                            driverViewModel.finishTrip()
                            backStack.clear()
                            backStack.add(DriverHome)
                            backStack.add(TripFinished(finishedTripId))
                        },
                        onBack = ::backToDriverHome
                    )
                }
            }

            entry<QrScannerMock> {
                QrScannerScreen(
                    routeName = driverState.activeTrip?.route?.name,
                    onSimulateValidRead = {
                        driverViewModel.validateToken(MockDriverData.demoToken)
                        backStack.add(BoardingResult)
                    },
                    onSimulateInvalidRead = {
                        driverViewModel.validateToken(MockDriverData.UNKNOWN_TOKEN)
                        backStack.add(BoardingResult)
                    },
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<BoardingResult> {
                driverState.lastValidation?.let { validation ->
                    val passenger = driverState.passengerBy(validation.reservationId)
                    BoardingResultScreen(
                        outcome = validation.outcome,
                        passenger = passenger,
                        routeName = passenger
                            ?.let { driverState.tripBy(it.tripId)?.route?.name }
                            .orEmpty(),
                        onRegisterBoarding = {
                            validation.reservationId?.let(driverViewModel::registerBoarding)
                        },
                        onScanAgain = {
                            driverViewModel.clearValidation()
                            backStack.removeLastOrNull()
                        },
                        onBackToTrip = {
                            driverViewModel.clearValidation()
                            openActiveTrip()
                        }
                    )
                }
            }

            entry<DriverPassengers> {
                DriverPassengersScreen(
                    passengers = driverState.activeTripId
                        ?.let(driverState::passengersFor)
                        .orEmpty(),
                    onConfirmCashPayment = driverViewModel::confirmCashPayment,
                    onRegisterBoarding = driverViewModel::registerBoarding,
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<ReportIncident> {
                ReportIncidentScreen(
                    onSubmit = driverViewModel::reportIncident,
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<TripFinished> { key ->
                driverState.tripBy(key.tripId)?.let { trip ->
                    TripFinishedScreen(
                        trip = trip,
                        boardedCount = driverState.boardedCountFor(trip.id),
                        passengerCount = driverState.passengersFor(trip.id).size,
                        anomalyCount = driverState.anomaliesFor(trip.id).size,
                        incidentCount = driverState.incidentsFor(trip.id).size,
                        onBackHome = ::backToDriverHome
                    )
                }
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
