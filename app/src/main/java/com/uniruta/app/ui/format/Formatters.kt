package com.uniruta.app.ui.format

import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val locale: Locale = Locale.forLanguageTag("es-CO")
private val longDate = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", locale)
private val shortDate = DateTimeFormatter.ofPattern("d MMM yyyy", locale)
private val time = DateTimeFormatter.ofPattern("h:mm a", locale)

fun LocalDate.asLongText(): String = format(longDate).replaceFirstChar { it.uppercase(locale) }

fun LocalDate.asShortText(): String = format(shortDate)

fun LocalTime.asText(): String = format(time).lowercase(locale)

fun LocalDateTime.asShortText(): String = "${toLocalDate().asShortText()}, ${toLocalTime().asText()}"

fun Int.asPriceText(): String = "$ ${NumberFormat.getIntegerInstance(locale).format(this)}"

fun Int.asDurationText(): String {
    val hours = this / 60
    val minutes = this % 60
    return when {
        hours == 0 -> "$minutes min"
        minutes == 0 -> "$hours h"
        else -> "$hours h $minutes min"
    }
}

fun Double.asDistanceText(): String = String.format(locale, "%.1f km", this)
