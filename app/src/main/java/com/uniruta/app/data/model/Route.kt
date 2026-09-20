package com.uniruta.app.data.model

data class Stop(
    val id: String,
    val name: String,
    val order: Int
)

data class Route(
    val id: String,
    val name: String,
    val origin: String,
    val destination: String,
    val stops: List<Stop>,
    val active: Boolean = true
)
