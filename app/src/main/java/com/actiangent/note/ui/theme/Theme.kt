package com.actiangent.note.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = darkGray,
    onPrimary = lightGray,
    secondary = darkGray,
    background = slightLightBlack
)

private val LightColorPalette = lightColors(
    primary = slightDarkWhite,
    onPrimary = slightLightBlack,
    secondary = lightGray,
    onSecondary = slightLightBlack,
    background = Color.White,
)

@Composable
fun NotesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val colors = if (darkTheme) {
        systemUiController.setStatusBarColor(
            color = DarkColorPalette.background,
            darkIcons = false
        )
        DarkColorPalette
    } else {
        systemUiController.setStatusBarColor(
            color = LightColorPalette.background,
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