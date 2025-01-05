package app.aloha.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFF1FDFF),
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF86D7E5),
    onPrimaryContainer = Color(0xFF000000),
    secondary = Color(0xFFF1FDFF),
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFFB5CFD4),
    onSecondaryContainer = Color(0xFF000000),
    tertiary = Color(0xFFFCFAFF),
    onTertiary = Color(0xFF000000),
    tertiaryContainer = Color(0xFFBECAEF),
    onTertiaryContainer = Color(0xFF000000),
    error = Color(0xFFFFF9F9),
    onError = Color(0xFF000000),
    errorContainer = Color(0xFFFFBAB1),
    onErrorContainer = Color(0xFF000000),
    background = Color(0xFF0E1415),
    onBackground = Color(0xFFDEE3E5),
    surface = Color(0xFF0E1415),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF3F484A),
    onSurfaceVariant = Color(0xFFF3FCFE),
    outline = Color(0xFFC3CCCE),
    inverseSurface = Color(0xFFDEE3E5),
    inverseOnSurface = Color(0xFF000000),
    inversePrimary = Color(0xFF002F35),
    surfaceTint = Color(0xFF82D3E0),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006874),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF9EEFFD),
    onPrimaryContainer = Color(0xFF001F24),
    secondary = Color(0xFF4A6267),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCDE7EC),
    onSecondaryContainer = Color(0xFF051F23),
    tertiary = Color(0xFF525E7D),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFDAE2FF),
    onTertiaryContainer = Color(0xFF0E1B37),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFF5FAFB),
    onBackground = Color(0xFF171D1E),
    surface = Color(0xFFF5FAFB),
    onSurface = Color(0xFF171D1E),
    surfaceVariant = Color(0xFFDBE4E6),
    onSurfaceVariant = Color(0xFF3F484A),
    outline = Color(0xFF6F797A),
    inverseSurface = Color(0xFF2B3133),
    inverseOnSurface = Color(0xFFECF2F3),
    inversePrimary = Color(0xFF82D3E0),
    surfaceTint = Color(0xFF006874)
)

@Composable
fun AlohaForumTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}