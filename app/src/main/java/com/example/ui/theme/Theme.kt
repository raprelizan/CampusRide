package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val CampusDarkColorScheme = darkColorScheme(
    primary = CampusEmerald,
    onPrimary = Color(0xFF003816),
    primaryContainer = CampusEmeraldContainer,
    onPrimaryContainer = Color(0xFF76FF9D),

    secondary = CampusCyan,
    onSecondary = Color(0xFF003547),
    secondaryContainer = Color(0xFF004D66),
    onSecondaryContainer = Color(0xFFBCE9FF),

    tertiary = CampusAmber,
    onTertiary = Color(0xFF452B00),
    tertiaryContainer = Color(0xFF634000),
    onTertiaryContainer = Color(0xFFFFDF9E),

    background = CampusNavyDark,
    onBackground = TextPrimaryDark,

    surface = CampusSurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = CampusCardDark,
    onSurfaceVariant = TextSecondaryDark,

    error = CampusCrimson,
    onError = Color.White,
    outline = CampusCardBorderDark
)

private val CampusLightColorScheme = lightColorScheme(
    primary = CampusEmeraldDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC7F9D4),
    onPrimaryContainer = Color(0xFF00210C),

    secondary = CampusCyanDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD4F3FF),
    onSecondaryContainer = Color(0xFF001F29),

    tertiary = Color(0xFFB45309),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFEDD5),
    onTertiaryContainer = Color(0xFF451A03),

    background = CampusNavyLight,
    onBackground = TextPrimaryLight,

    surface = CampusSurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondaryLight,

    error = CampusCrimson,
    onError = Color.White,
    outline = CampusCardBorderLight
)

@Composable
fun CampusRideTheme(
    darkTheme: Boolean = true, // Default to sleek modern dark theme for premium tech feel
    dynamicColor: Boolean = false, // Keep branded emerald & navy colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> CampusDarkColorScheme
        else -> CampusLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
