package org.walley.wteplota

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White

val w_light_primary = Color(0xFF825500)
val w_light_onPrimary = Color(0xFFFFFFFF)
val w_light_primaryContainer = Color(0xFFFFDDB3)
val w_light_onPrimaryContainer = Color(0xFF291800)
val w_light_secondary = Color(0xFF6F5B40)
val w_light_onSecondary = Color(0xFFFFFFFF)
val w_light_secondaryContainer = Color(0xFFFBDEBC)
val w_light_onSecondaryContainer = Color(0xFF271904)
val w_light_tertiary = Color(0xFF51643F)
val w_light_onTertiary = Color(0xFFFFFFFF)
val w_light_tertiaryContainer = Color(0xFFD4EABB)
val w_light_onTertiaryContainer = Color(0xFF102004)
val w_light_error = Color(0xFFBA1A1A)
val w_light_errorContainer = Color(0xFFFFDAD6)
val w_light_onError = Color(0xFFFFFFFF)
val w_light_onErrorContainer = Color(0xFF410002)
val w_light_background = Color(0xFFFFFBFF)
val w_light_onBackground = Color(0xFF1F1B16)
val w_light_surface = Color(0xFFFFFBFF)
val w_light_onSurface = Color(0xFF1F1B16)
val w_light_surfaceVariant = Color(0xFFF0E0CF)
val w_light_onSurfaceVariant = Color(0xFF4F4539)
val w_light_outline = Color(0xFF817567)
val w_light_inverseOnSurface = Color(0xFFF9EFE7)
val w_light_inverseSurface = Color(0xFF34302A)
val w_light_inversePrimary = Color(0xFFFFB951)
val w_light_shadow = Color(0xFF000000)
val w_light_surfaceTint = Color(0xFF825500)
val w_light_outlineVariant = Color(0xFFD3C4B4)
val w_light_scrim = Color(0xFF000000)

val w_dark_primary = Color(0xFFFFB951)
val w_dark_onPrimary = Color(0xFF452B00)
val w_dark_primaryContainer = Color(0xFF633F00)
val w_dark_onPrimaryContainer = Color(0xFFFFDDB3)
val w_dark_secondary = Color(0xFFDDC2A1)
val w_dark_onSecondary = Color(0xFF3E2D16)
val w_dark_secondaryContainer = Color(0xFF56442A)
val w_dark_onSecondaryContainer = Color(0xFFFBDEBC)
val w_dark_tertiary = Color(0xFFB8CEA1)
val w_dark_onTertiary = Color(0xFF243515)
val w_dark_tertiaryContainer = Color(0xFF3A4C2A)
val w_dark_onTertiaryContainer = Color(0xFFD4EABB)
val w_dark_error = Color(0xFFFFB4AB)
val w_dark_errorContainer = Color(0xFF93000A)
val w_dark_onError = Color(0xFF690005)
val w_dark_onErrorContainer = Color(0xFFFFDAD6)
val w_dark_background = Color(0xFF1F1B16)
val w_dark_onBackground = Color(0xFFEAE1D9)
val w_dark_surface = Color(0xFF1F1B16)
val w_dark_onSurface = Color(0xFFEAE1D9)
val w_dark_surfaceVariant = Color(0xFF4F4539)
val w_dark_onSurfaceVariant = Color(0xFFD3C4B4)
val w_dark_outline = Color(0xFF9C8F80)
val w_dark_inverseOnSurface = Color(0xFF1F1B16)
val w_dark_inverseSurface = Color(0xFFEAE1D9)
val w_dark_inversePrimary = Color(0xFF825500)
val w_dark_shadow = Color(0xFF000000)
val w_dark_surfaceTint = Color(0xFFFFB951)
val w_dark_outlineVariant = Color(0xFF4F4539)
val w_dark_scrim = Color(0xFF000000)

val red200 = Color(0XFFEF9A9A)
val red500 = Color(0XFFF44336)
val red700 = Color(0XFFD32F2F)
val pink200 = Color(0XFFF27584)
val pink500 = Color(0XFFEF5366)

val pink700 = Color(0XFFD74A5B)
val purple200 = Color(0XFFCE93D8)
val purple500 = Color(0XFF9C27B0)
val purple700 = Color(0XFF7B1FA2)

val indigo200 = Color(0XFF9FA8DA)
val indigo500 = Color(0XFF3F51B5)
val indigo700 = Color(0XFF303f9f)
val blue200 = Color(0XFF90CAF9)
val blue500 = Color(0xFF2195F2)
val blue700 = Color(0xFF1976D2)

val teal200 = Color(0XFF80DEEA)

val green200 = Color(0XFFA5D6A7)
val green500 = Color(0XFF4CAF50)
val green700 = Color(0XFF388E3C)

val yellow200 = Color(0XFFFFF59D)
val yellow500 = Color(0XFFFFEB3B)
val yellow700 = Color(0XFFFBC02D)

val orange200 = Color(0XFFFFCC80)
val orange500 = Color(0XFFFF9800)
val orange700 = Color(0XFFF57C00)

val brown200 = Color(0XFFBCAAA4)
val brown500 = Color(0XFF795548)
val brown700 = Color(0XFF5D4037)
val grey200 = Color(0XFFEEEEEE)
val grey500 = Color(0XFF9E9E9E)
val grey700 = Color(0XFF616161)

public val DarkOrangeColorPalette = darkColorScheme(
  primary = orange200,
  secondary = orange700,
  tertiary = teal200,
  background = Black,
  surface = Black,
  onPrimary = Black,
  onTertiary = White,
  onBackground = White,
  onSurface = White,
  error = Red,
)

private val LightColors = lightColorScheme(
  primary = w_light_primary,
  onPrimary = w_light_onPrimary,
  primaryContainer = w_light_primaryContainer,
  onPrimaryContainer = w_light_onPrimaryContainer,
  secondary = w_light_secondary,
  onSecondary = w_light_onSecondary,
  secondaryContainer = w_light_secondaryContainer,
  onSecondaryContainer = w_light_onSecondaryContainer,
  tertiary = w_light_tertiary,
  onTertiary = w_light_onTertiary,
  tertiaryContainer = w_light_tertiaryContainer,
  onTertiaryContainer = w_light_onTertiaryContainer,
  error = w_light_error,
  errorContainer = w_light_errorContainer,
  onError = w_light_onError,
  onErrorContainer = w_light_onErrorContainer,
  background = w_light_background,
  onBackground = w_light_onBackground,
  surface = w_light_surface,
  onSurface = w_light_onSurface,
  surfaceVariant = w_light_surfaceVariant,
  onSurfaceVariant = w_light_onSurfaceVariant,
  outline = w_light_outline,
  inverseOnSurface = w_light_inverseOnSurface,
  inverseSurface = w_light_inverseSurface,
  inversePrimary = w_light_inversePrimary,
  surfaceTint = w_light_surfaceTint,
  outlineVariant = w_light_outlineVariant,
  scrim = w_light_scrim,
)

private val DarkColors = darkColorScheme(
  primary = w_dark_primary,
  onPrimary = w_dark_onPrimary,
  primaryContainer = w_dark_primaryContainer,
  onPrimaryContainer = w_dark_onPrimaryContainer,
  secondary = w_dark_secondary,
  onSecondary = w_dark_onSecondary,
  secondaryContainer = w_dark_secondaryContainer,
  onSecondaryContainer = w_dark_onSecondaryContainer,
  tertiary = w_dark_tertiary,
  onTertiary = w_dark_onTertiary,
  tertiaryContainer = w_dark_tertiaryContainer,
  onTertiaryContainer = w_dark_onTertiaryContainer,
  error = w_dark_error,
  errorContainer = w_dark_errorContainer,
  onError = w_dark_onError,
  onErrorContainer = w_dark_onErrorContainer,
  background = w_dark_background,
  onBackground = w_dark_onBackground,
  surface = w_dark_surface,
  onSurface = w_dark_onSurface,
  surfaceVariant = w_dark_surfaceVariant,
  onSurfaceVariant = w_dark_onSurfaceVariant,
  outline = w_dark_outline,
  inverseOnSurface = w_dark_inverseOnSurface,
  inverseSurface = w_dark_inverseSurface,
  inversePrimary = w_dark_inversePrimary,
  surfaceTint = w_dark_surfaceTint,
  outlineVariant = w_dark_outlineVariant,
  scrim = w_dark_scrim,
)

@Composable
fun AppTheme(
  useDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit
) {
  val colors = if (!useDarkTheme) {
    LightColors
  } else {
    DarkOrangeColorPalette
  }

  MaterialTheme(
    colorScheme = colors, content = content
  )
}

