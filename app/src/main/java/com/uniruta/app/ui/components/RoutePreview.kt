package com.uniruta.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun RoutePreview(
    origin: String,
    destination: String,
    stopCount: Int,
    modifier: Modifier = Modifier
) {
    val lineColor = MaterialTheme.colorScheme.primary
    val markerColor = MaterialTheme.colorScheme.secondary
    val gridColor = MaterialTheme.colorScheme.outline

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Vista del recorrido",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp)
                    .padding(top = 12.dp, bottom = 12.dp)
            ) {
                val dashed = PathEffect.dashPathEffect(floatArrayOf(6f, 10f))
                for (index in 1..3) {
                    val y = size.height * index / 4f
                    drawLine(
                        color = gridColor.copy(alpha = 0.25f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f,
                        pathEffect = dashed
                    )
                }

                val points = routePoints(stopCount.coerceAtLeast(2))
                val path = Path()
                points.forEachIndexed { index, point ->
                    val x = point.x * size.width
                    val y = point.y * size.height
                    if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                drawPath(path = path, color = lineColor, style = Stroke(width = 6f))

                points.forEachIndexed { index, point ->
                    val center = Offset(point.x * size.width, point.y * size.height)
                    val isEnd = index == 0 || index == points.lastIndex
                    drawCircle(
                        color = if (isEnd) lineColor else markerColor,
                        radius = if (isEnd) 9f else 6f,
                        center = center
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = origin,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = destination,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Text(
                text = "El mapa se integrará en una fase posterior.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

private fun routePoints(count: Int): List<Offset> {
    val verticalPattern = listOf(0.72f, 0.34f, 0.60f, 0.24f, 0.52f, 0.30f)
    return List(count) { index ->
        Offset(
            x = 0.06f + (0.88f * index / (count - 1).coerceAtLeast(1)),
            y = verticalPattern[index % verticalPattern.size]
        )
    }
}
