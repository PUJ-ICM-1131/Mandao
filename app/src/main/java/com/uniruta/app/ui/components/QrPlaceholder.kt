package com.uniruta.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.abs

private const val MODULES = 25

@Composable
fun QrPlaceholder(
    token: String,
    modifier: Modifier = Modifier
) {
    val matrix = remember(token) { buildMatrix(token) }

    Surface(
        color = Color.White,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        Box(modifier = Modifier.padding(14.dp)) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                val cell = size.width / MODULES
                for (row in 0 until MODULES) {
                    for (column in 0 until MODULES) {
                        if (!matrix[row * MODULES + column]) continue
                        drawRect(
                            color = Color.Black,
                            topLeft = Offset(column * cell, row * cell),
                            size = Size(cell, cell)
                        )
                    }
                }
            }
        }
    }
}

private fun buildMatrix(token: String): BooleanArray {
    val cells = BooleanArray(MODULES * MODULES)
    var seed = token.hashCode().toLong() * 2862933555777941757L + 3037000493L

    for (index in cells.indices) {
        seed = seed * 6364136223846793005L + 1442695040888963407L
        cells[index] = (seed ushr 41) % 2L == 0L
    }

    val finderOrigins = listOf(0 to 0, 0 to MODULES - 7, MODULES - 7 to 0)
    for ((originRow, originColumn) in finderOrigins) {
        for (row in -1..7) {
            for (column in -1..7) {
                val targetRow = originRow + row
                val targetColumn = originColumn + column
                if (targetRow !in 0 until MODULES || targetColumn !in 0 until MODULES) continue
                cells[targetRow * MODULES + targetColumn] = finderModule(row, column)
            }
        }
    }
    return cells
}

private fun finderModule(row: Int, column: Int): Boolean {
    if (row !in 0..6 || column !in 0..6) return false
    return maxOf(abs(row - 3), abs(column - 3)) != 2
}
