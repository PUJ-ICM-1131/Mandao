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

fun homeDestinationFor(role: UserRole): NavKey = when (role) {
    UserRole.STUDENT -> StudentHome
    UserRole.DRIVER -> DriverHome
    UserRole.ADMIN -> AdminHome
}
