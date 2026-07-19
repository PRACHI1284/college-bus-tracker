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

private val DarkColorScheme =
  darkColorScheme(
    primary = GeometricPrimary,
    onPrimary = GeometricBackground,
    primaryContainer = GeometricPrimaryContainer,
    onPrimaryContainer = GeometricOnSurface,
    secondary = GeometricPrimaryContainer,
    onSecondary = GeometricOnSurface,
    secondaryContainer = GeometricSecondaryContainer,
    onSecondaryContainer = GeometricOnSurface,
    background = Color(0xFF111418),
    onBackground = GeometricBackground,
    surface = Color(0xFF111418),
    onSurface = GeometricBackground,
    surfaceVariant = Color(0xFF20232A),
    onSurfaceVariant = GeometricBorder,
    outline = GeometricBorder,
    outlineVariant = GeometricDivider
  )

private val LightColorScheme =
  lightColorScheme(
    primary = GeometricPrimary,
    onPrimary = Color.White,
    primaryContainer = GeometricPrimaryContainer,
    onPrimaryContainer = GeometricOnSurface,
    secondary = GeometricPrimary,
    onSecondary = Color.White,
    secondaryContainer = GeometricSecondaryContainer,
    onSecondaryContainer = GeometricOnSurface,
    background = GeometricBackground,
    onBackground = GeometricOnSurface,
    surface = GeometricBackground,
    onSurface = GeometricOnSurface,
    surfaceVariant = GeometricSurfaceVariant,
    onSurfaceVariant = Color(0xFF44474E),
    outline = Color(0xFF74777F),
    outlineVariant = GeometricBorder
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Set dynamicColor to false by default to ensure Geometric Balance styling takes center stage
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
