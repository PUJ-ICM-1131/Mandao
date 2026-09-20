package com.uniruta.app.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uniruta.app.data.model.PassStatus
import com.uniruta.app.data.model.PaymentStatus
import com.uniruta.app.data.model.ReservationStatus
import com.uniruta.app.ui.theme.OnSuccessContainerDark
import com.uniruta.app.ui.theme.OnSuccessContainerLight
import com.uniruta.app.ui.theme.SuccessContainerDark
import com.uniruta.app.ui.theme.SuccessContainerLight

enum class StatusTone { NEUTRAL, POSITIVE, WARNING, NEGATIVE }

@Composable
fun StatusChip(
    label: String,
    tone: StatusTone,
    modifier: Modifier = Modifier
) {
    val dark = isSystemInDarkTheme()
    val container: Color
    val content: Color
    when (tone) {
        StatusTone.POSITIVE -> {
            container = if (dark) SuccessContainerDark else SuccessContainerLight
            content = if (dark) OnSuccessContainerDark else OnSuccessContainerLight
        }
        StatusTone.WARNING -> {
            container = MaterialTheme.colorScheme.secondaryContainer
            content = MaterialTheme.colorScheme.onSecondaryContainer
        }
        StatusTone.NEGATIVE -> {
            container = MaterialTheme.colorScheme.errorContainer
            content = MaterialTheme.colorScheme.onErrorContainer
        }
        StatusTone.NEUTRAL -> {
            container = MaterialTheme.colorScheme.surfaceVariant
            content = MaterialTheme.colorScheme.onSurfaceVariant
        }
    }

    Surface(
        color = container,
        contentColor = content,
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

fun ReservationStatus.tone(): StatusTone = when (this) {
    ReservationStatus.PENDING -> StatusTone.WARNING
    ReservationStatus.CONFIRMED, ReservationStatus.BOARDED, ReservationStatus.COMPLETED -> StatusTone.POSITIVE
    ReservationStatus.CANCELLED -> StatusTone.NEGATIVE
}

fun PaymentStatus.tone(): StatusTone = when (this) {
    PaymentStatus.PENDING -> StatusTone.NEUTRAL
    PaymentStatus.UNDER_REVIEW -> StatusTone.WARNING
    PaymentStatus.CONFIRMED -> StatusTone.POSITIVE
    PaymentStatus.REJECTED -> StatusTone.NEGATIVE
}

fun PassStatus.tone(): StatusTone = when (this) {
    PassStatus.ACTIVE -> StatusTone.POSITIVE
    PassStatus.USED -> StatusTone.NEUTRAL
    PassStatus.EXPIRED -> StatusTone.NEGATIVE
}
