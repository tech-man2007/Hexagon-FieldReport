package com.hexagon.fieldreport.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlassContainer(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()

    // Highly solid colors to make it hard to see through (thick frost)
    val gradientColors = if (isDark) {
        listOf(
            // A thick, dark fill that blends well with your navy/teal background
            Color.Black.copy(alpha = 0.75f),
            Color.Black.copy(alpha = 0.60f)
        )
    } else {
        listOf(
            // A thick, milky white fill
            Color.White.copy(alpha = 0.90f),
            Color.White.copy(alpha = 0.75f)
        )
    }

    // Keep the subtle border to retain the "glass edge" reflection
    val borderColor = if (isDark) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.6f)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(Brush.linearGradient(colors = gradientColors))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            ),
        content = content
    )
}