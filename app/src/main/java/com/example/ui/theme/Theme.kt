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
    primary = BentoBlueLight,
    secondary = BentoDarkBadge,
    tertiary = BentoRoseCard,
    background = Color(0xFF0F1117),
    surface = BentoDarkCard,
    onBackground = Color.White,
    onSurface = Color.White,
    outline = Color(0xFF2E323E)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = BentoBluePrimary,
    secondary = BentoTextSecondary,
    tertiary = BentoRoseAccent,
    background = BentoBackground,
    surface = BentoSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = BentoTextPrimary,
    onSurface = BentoTextPrimary,
    surfaceVariant = BentoSurfaceVariant,
    outline = BentoBorder
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false, // Default to pristine Bento Grid Light Theme from design
  dynamicColor: Boolean = false, // Honor the Bento Grid custom aesthetic precisely
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
