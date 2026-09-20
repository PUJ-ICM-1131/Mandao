package com.uniruta.app.data.mock

import com.uniruta.app.data.model.Route
import com.uniruta.app.data.model.Stop
import com.uniruta.app.data.model.Trip
import java.time.LocalDate
import java.time.LocalTime

object MockTrips {

    private const val DESTINATION = "Pontificia Universidad Javeriana"

    private val chiaRoute = Route(
        id = "route-chia",
        name = "Chía → Javeriana",
        origin = "Chía",
        destination = DESTINATION,
        stops = listOf(
            Stop("stop-ch-1", "Chía - Parque Central", 1),
            Stop("stop-ch-2", "Puente del Común", 2),
            Stop("stop-ch-3", "Autopista Norte - Calle 170", 3),
            Stop("stop-ch-4", "Calle 100", 4),
            Stop("stop-ch-5", "Javeriana - Carrera 7", 5)
        )
    )

    private val cedritosRoute = Route(
        id = "route-cedritos",
        name = "Cedritos → Javeriana",
        origin = "Cedritos",
        destination = DESTINATION,
        stops = listOf(
            Stop("stop-ce-1", "Cedritos - Calle 140", 1),
            Stop("stop-ce-2", "Calle 127", 2),
            Stop("stop-ce-3", "Calle 116", 3),
            Stop("stop-ce-4", "Héroes", 4),
            Stop("stop-ce-5", "Javeriana - Carrera 7", 5)
        )
    )

    private val subaRoute = Route(
        id = "route-suba",
        name = "Suba → Javeriana",
        origin = "Suba",
        destination = DESTINATION,
        stops = listOf(
            Stop("stop-su-1", "Suba - Centro", 1),
            Stop("stop-su-2", "Portal Suba", 2),
            Stop("stop-su-3", "Calle 80", 3),
            Stop("stop-su-4", "Avenida Caracas", 4),
            Stop("stop-su-5", "Javeriana - Carrera 7", 5)
        )
    )

    private val modeliaRoute = Route(
        id = "route-modelia",
        name = "Modelia → Javeriana",
        origin = "Modelia",
        destination = DESTINATION,
        stops = listOf(
            Stop("stop-mo-1", "Modelia - Avenida La Esperanza", 1),
            Stop("stop-mo-2", "Avenida Boyacá", 2),
            Stop("stop-mo-3", "Salitre", 3),
            Stop("stop-mo-4", "Universidad Nacional", 4),
            Stop("stop-mo-5", "Javeriana - Carrera 7", 5)
        )
    )

    private val today: LocalDate = LocalDate.now()
    private val tomorrow: LocalDate = today.plusDays(1)

    val all: List<Trip> = listOf(
        Trip(
            id = "trip-001",
            route = chiaRoute,
            date = today,
            departureTime = LocalTime.of(6, 30),
            availableSeats = 12,
            totalSeats = 25,
            vehiclePlate = "TUV-421",
            driverName = "Carlos Medina",
            estimatedDurationMinutes = 55,
            estimatedDistanceKm = 28.4,
            price = 8500
        ),
        Trip(
            id = "trip-002",
            route = cedritosRoute,
            date = today,
            departureTime = LocalTime.of(7, 0),
            availableSeats = 5,
            totalSeats = 20,
            vehiclePlate = "WXY-118",
            driverName = "Marcela Ríos",
            estimatedDurationMinutes = 40,
            estimatedDistanceKm = 12.6,
            price = 5500
        ),
        Trip(
            id = "trip-003",
            route = subaRoute,
            date = today,
            departureTime = LocalTime.of(6, 45),
            availableSeats = 0,
            totalSeats = 22,
            vehiclePlate = "GHT-905",
            driverName = "Andrés Lozano",
            estimatedDurationMinutes = 50,
            estimatedDistanceKm = 17.2,
            price = 6000
        ),
        Trip(
            id = "trip-004",
            route = modeliaRoute,
            date = tomorrow,
            departureTime = LocalTime.of(7, 15),
            availableSeats = 18,
            totalSeats = 25,
            vehiclePlate = "KLP-337",
            driverName = "Diana Castro",
            estimatedDurationMinutes = 45,
            estimatedDistanceKm = 15.8,
            price = 6000
        ),
        Trip(
            id = "trip-005",
            route = chiaRoute,
            date = tomorrow,
            departureTime = LocalTime.of(7, 30),
            availableSeats = 9,
            totalSeats = 25,
            vehiclePlate = "TUV-421",
            driverName = "Carlos Medina",
            estimatedDurationMinutes = 60,
            estimatedDistanceKm = 28.4,
            price = 8500
        )
    )
}
