package com.example.calcora.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.calcora.theme.LocalGlassColors

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    elevation: Dp = 4.dp,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val glassColors = LocalGlassColors.current
    val shape = RoundedCornerShape(cornerRadius)
    val bg = backgroundColor ?: glassColors.cardBackground
    val border = borderColor ?: glassColors.cardBorder

    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(color = glassColors.primaryBlue.copy(alpha = 0.2f)),
            onClick = onClick
        )
    } else Modifier

    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = glassColors.glowColor,
                spotColor = glassColors.glowColor
            )
            .clip(shape)
            .background(bg)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        border.copy(alpha = 0.7f),
                        border.copy(alpha = 0.2f)
                    )
                ),
                shape = shape
            )
            .then(clickableModifier),
        content = content
    )
}
