package com.uniruta.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun QrScannerFrame(modifier: Modifier = Modifier) {
    val frameColor = MaterialTheme.colorScheme.primary
    val glyphColor = MaterialTheme.colorScheme.onSurfaceVariant
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        drawRoundRect(
            color = surfaceColor,
            cornerRadius = CornerRadius(24f, 24f)
        )

        val arm = size.minDimension * 0.18f
        val inset = size.minDimension * 0.08f
        val stroke = Stroke(width = 10f, cap = StrokeCap.Round)
        val corners = listOf(
            Offset(inset, inset) to listOf(Offset(inset + arm, inset), Offset(inset, inset + arm)),
            Offset(size.width - inset, inset) to
                listOf(Offset(size.width - inset - arm, inset), Offset(size.width - inset, inset + arm)),
            Offset(inset, size.height - inset) to
                listOf(Offset(inset + arm, size.height - inset), Offset(inset, size.height - inset - arm)),
            Offset(size.width - inset, size.height - inset) to
                listOf(
                    Offset(size.width - inset - arm, size.height - inset),
                    Offset(size.width - inset, size.height - inset - arm)
                )
        )
        corners.forEach { (corner, arms) ->
            val path = Path()
            path.moveTo(arms[0].x, arms[0].y)
            path.lineTo(corner.x, corner.y)
            path.lineTo(arms[1].x, arms[1].y)
            drawPath(path = path, color = frameColor, style = stroke)
        }

        val glyph = size.minDimension * 0.16f
        val gap = size.minDimension * 0.06f
        val originX = size.width / 2f - glyph - gap / 2f
        val originY = size.height / 2f - glyph - gap / 2f
        val glyphOrigins = listOf(
            Offset(originX, originY),
            Offset(originX + glyph + gap, originY),
            Offset(originX, originY + glyph + gap)
        )
        glyphOrigins.forEach { origin ->
            drawRect(
                color = glyphColor,
                topLeft = origin,
                size = Size(glyph, glyph),
                style = Stroke(width = 6f)
            )
            drawRect(
                color = glyphColor,
                topLeft = Offset(origin.x + glyph * 0.3f, origin.y + glyph * 0.3f),
                size = Size(glyph * 0.4f, glyph * 0.4f)
            )
        }
    }
}
