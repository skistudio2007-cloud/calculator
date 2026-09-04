package com.example.calcora.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class GlassColors(
    val cardBackground: Color,
    val cardBorder: Color,
    val numberButtonBg: Color,
    val numberButtonText: Color,
    val operatorButtonBg: Color,
    val operatorButtonText: Color,
    val primaryBlue: Color,
    val displayBackground: Color,
    val secondaryBackground: Color,
    val secondaryText: Color,
    val glowColor: Color
)

val LocalGlassColors = staticCompositionLocalOf {
    GlassColors(
        cardBackground = LightGlassCard,
        cardBorder = LightGlassBorder,
        numberButtonBg = LightNumberButtonBg,
        numberButtonText = LightPrimaryText,
        operatorButtonBg = LightOperatorButtonBg,
        operatorButtonText = PrimaryBlue,
        primaryBlue = PrimaryBlue,
        displayBackground = Color.White.copy(alpha = 0.7f),
        secondaryBackground = LightSecondaryBackground,
        secondaryText = LightSecondaryText,
        glowColor = LightAmbientGlow
    )
}

private val CalcoraLightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = LightAccentGlow,
    onPrimaryContainer = DarkBlue,
    secondary = PremiumLightBlue,
    onSecondary = Color.White,
    background = LightPrimaryBackground,
    onBackground = LightPrimaryText,
    surface = LightSecondaryBackground,
    onSurface = LightPrimaryText,
    surfaceVariant = LightAmbientGlow,
    onSurfaceVariant = LightSecondaryText,
    outline = LightGlassBorder
)

private val CalcoraDarkColorScheme = darkColorScheme(
    primary = DarkBlueAccent,
    onPrimary = DarkPrimaryBackground,
    primaryContainer = DarkGlassSurface,
    onPrimaryContainer = DarkPrimaryText,
    secondary = SoftBlue,
    onSecondary = DarkPrimaryBackground,
    background = DarkPrimaryBackground,
    onBackground = DarkPrimaryText,
    surface = DarkSecondaryBackground,
    onSurface = DarkPrimaryText,
    surfaceVariant = DarkGlassSurface,
    onSurfaceVariant = DarkSecondaryText,
    outline = DarkGlassBorder
)

@Composable
fun CalcoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) CalcoraDarkColorScheme else CalcoraLightColorScheme

    val glassColors = if (darkTheme) {
        GlassColors(
            cardBackground = DarkGlassCard,
            cardBorder = DarkGlassBorder,
            numberButtonBg = DarkNumberButtonBg,
            numberButtonText = DarkPrimaryText,
            operatorButtonBg = DarkOperatorButtonBg,
            operatorButtonText = DarkBlueAccent,
            primaryBlue = DarkBlueAccent,
            displayBackground = DarkGlassSurface.copy(alpha = 0.85f),
            secondaryBackground = DarkSecondaryBackground,
            secondaryText = DarkSecondaryText,
            glowColor = Color(0x1A5CA9FF)
        )
    } else {
        GlassColors(
            cardBackground = LightGlassCard,
            cardBorder = LightGlassBorder,
            numberButtonBg = LightNumberButtonBg,
            numberButtonText = LightPrimaryText,
            operatorButtonBg = LightOperatorButtonBg,
            operatorButtonText = PrimaryBlue,
            primaryBlue = PrimaryBlue,
            displayBackground = Color.White.copy(alpha = 0.75f),
            secondaryBackground = LightSecondaryBackground,
            secondaryText = LightSecondaryText,
            glowColor = LightAmbientGlow
        )
    }

    CompositionLocalProvider(LocalGlassColors provides glassColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = CalcoraTypography,
            content = content
        )
    }
}
