package com.uniruta.app.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.uniruta.app.data.mock.MockDriverData
import com.uniruta.app.data.model.MockUser
import com.uniruta.app.ui.screens.driver.ActiveTripScreen
import com.uniruta.app.ui.screens.driver.BoardingResultScreen
import com.uniruta.app.ui.screens.driver.DriverHomeScreen
import com.uniruta.app.ui.screens.driver.DriverPassengersScreen
import com.uniruta.app.ui.screens.driver.DriverTripDetailScreen
import com.uniruta.app.ui.screens.driver.DriverTripsScreen
import com.uniruta.app.ui.screens.driver.QrScannerScreen
import com.uniruta.app.ui.screens.driver.ReportIncidentScreen
import com.uniruta.app.ui.screens.driver.TripFinishedScreen
import com.uniruta.app.viewmodel.DriverUiState
import com.uniruta.app.viewmodel.DriverViewModel

fun EntryProviderScope<NavKey>.driverEntries(
    state: DriverUiState,
    viewModel: DriverViewModel,
    user: MockUser?,
    onLogout: () -> Unit,
    backStack: NavBackStack<NavKey>
) {
    fun backToHome() {
        backStack.clear()
        backStack.add(DriverHome)
    }

    fun openActiveTrip() {
        backStack.clear()
        backStack.add(DriverHome)
        backStack.add(ActiveTrip)
    }

    entry<DriverHome> {
        user?.let { current ->
            DriverHomeScreen(
                user = current,
                hasActiveTrip = state.activeTrip != null,
                onOpenTrips = { backStack.add(DriverTrips) },
                onStartTrip = {
                    val startable = state.startableTrip
                    when {
                        state.activeTrip != null -> openActiveTrip()
                        startable != null -> backStack.add(DriverTripDetail(startable.id))
                        else -> backStack.add(DriverTrips)
                    }
                },
                onValidateBoarding = { backStack.add(QrScannerMock) },
                onLogout = onLogout
            )
        }
    }

    entry<DriverTrips> {
        DriverTripsScreen(
            trips = state.trips,
            driverName = user?.name.orEmpty(),
            onTripSelected = { backStack.add(DriverTripDetail(it)) },
            onBack = { backStack.removeLastOrNull() }
        )
    }

    entry<DriverTripDetail> { key ->
        state.tripBy(key.tripId)?.let { trip ->
            DriverTripDetailScreen(
                trip = trip,
                passengerCount = state.passengersFor(trip.id).size,
                startBlockedReason = if (state.activeTripId != null) {
                    "Ya tienes un recorrido en curso. Finalízalo antes de iniciar otro."
                } else {
                    null
                },
                onStartTrip = {
                    viewModel.startTrip(trip.id)
                    openActiveTrip()
                },
                onOpenActiveTrip = ::openActiveTrip,
                onBack = { backStack.removeLastOrNull() }
            )
        }
    }

    entry<ActiveTrip> {
        state.activeTrip?.let { trip ->
            ActiveTripScreen(
                trip = trip,
                gpsStatus = state.gpsStatus,
                sensorStatus = state.sensorStatus,
                boardedCount = state.boardedCountFor(trip.id),
                passengerCount = state.passengersFor(trip.id).size,
                anomalies = state.anomaliesFor(trip.id),
                incidentCount = state.incidentsFor(trip.id).size,
                onValidateBoarding = { backStack.add(QrScannerMock) },
                onOpenPassengers = { backStack.add(DriverPassengers) },
                onReportIncident = { backStack.add(ReportIncident) },
                onFinishTrip = {
                    val finishedTripId = trip.id
                    viewModel.finishTrip()
                    backStack.clear()
                    backStack.add(DriverHome)
                    backStack.add(TripFinished(finishedTripId))
                },
                onBack = ::backToHome
            )
        }
    }

    entry<QrScannerMock> {
        QrScannerScreen(
            routeName = state.activeTrip?.route?.name,
            onSimulateValidRead = {
                viewModel.validateToken(MockDriverData.demoToken)
                backStack.add(BoardingResult)
            },
            onSimulateInvalidRead = {
                viewModel.validateToken(MockDriverData.UNKNOWN_TOKEN)
                backStack.add(BoardingResult)
            },
            onBack = { backStack.removeLastOrNull() }
        )
    }

    entry<BoardingResult> {
        state.lastValidation?.let { validation ->
            val passenger = state.passengerBy(validation.reservationId)
            BoardingResultScreen(
                outcome = validation.outcome,
                passenger = passenger,
                routeName = passenger
                    ?.let { state.tripBy(it.tripId)?.route?.name }
                    .orEmpty(),
                onRegisterBoarding = {
                    validation.reservationId?.let(viewModel::registerBoarding)
                },
                onScanAgain = {
                    viewModel.clearValidation()
                    backStack.removeLastOrNull()
                },
                onBackToTrip = {
                    viewModel.clearValidation()
                    openActiveTrip()
                }
            )
        }
    }

    entry<DriverPassengers> {
        DriverPassengersScreen(
            passengers = state.activeTripId
                ?.let(state::passengersFor)
                .orEmpty(),
            onConfirmCashPayment = viewModel::confirmCashPayment,
            onRegisterBoarding = viewModel::registerBoarding,
            onBack = { backStack.removeLastOrNull() }
        )
    }

    entry<ReportIncident> {
        ReportIncidentScreen(
            onSubmit = viewModel::reportIncident,
            onBack = { backStack.removeLastOrNull() }
        )
    }

    entry<TripFinished> { key ->
        state.tripBy(key.tripId)?.let { trip ->
            TripFinishedScreen(
                trip = trip,
                boardedCount = state.boardedCountFor(trip.id),
                passengerCount = state.passengersFor(trip.id).size,
                anomalyCount = state.anomaliesFor(trip.id).size,
                incidentCount = state.incidentsFor(trip.id).size,
                onBackHome = ::backToHome
            )
        }
    }
}
