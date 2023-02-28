package com.actiangent.note.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = slightDarkWhite,
    primaryVariant = slightLightBlack,
    secondary = darkGray,
    background = slightLightBlack,
    surface = slightLightBlack
)

private val LightColorPalette = lightColors(
    primary = slightLightBlack,
    primaryVariant = slightDarkWhite,
    secondary = lightGray,
    background = slightDarkWhite,
    surface = slightDarkWhite,
)

@Composable
fun NotesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val colors = if (darkTheme) {
        systemUiController.setStatusBarColor(
            color = DarkColorPalette.primaryVariant,
            darkIcons = false
        )
        DarkColorPalette
    } else {
        systemUiController.setStatusBarColor(
            color = LightColorPalette.primaryVariant,
            darkIcons = true
        )
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
    ) {
        CompositionLocalProvider(
            LocalIndication provides rememberRipple(color = colors.secondary),
            content = content,
        )
    }
}