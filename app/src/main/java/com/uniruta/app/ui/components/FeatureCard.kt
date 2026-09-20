package com.uniruta.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class FeatureItem(
    val title: String,
    val description: String,
    val icon: ImageVector? = null,
    val onClick: (() -> Unit)? = null
)

@Composable
fun FeatureCard(
    item: FeatureItem,
    modifier: Modifier = Modifier
) {
    val onClick = item.onClick
    val cardModifier = if (onClick != null) {
        modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    } else {
        modifier.fillMaxWidth()
    }

    Card(
        modifier = cardModifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.icon != null) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(22.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = if (item.icon != null) 14.dp else 0.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (onClick != null) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
