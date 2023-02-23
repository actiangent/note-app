package com.actiangent.note.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.actiangent.note.ui.navigation.ROUTE_DETAIL_ARG
import com.actiangent.note.ui.navigation.Screen
import com.actiangent.note.ui.screen.detailnote.DetailNoteScreen
import com.actiangent.note.ui.screen.home.HomeScreen
import com.actiangent.note.ui.theme.NotesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesAppTheme {
                NotesApp()
            }
        }
    }
}

@Composable
fun NotesApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                modifier = modifier.padding(start = 8.dp, end = 8.dp),
                navigateToDetailNote = { noteId ->
                    navController.navigate(Screen.DetailNote.createDetailNoteRoute(noteId))
                }
            )
        }
        composable(
            route = Screen.DetailNote.route,
            arguments = listOf(navArgument(ROUTE_DETAIL_ARG) { type = NavType.IntType })
        ) {
            DetailNoteScreen(
                modifier = modifier,
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesAppPreview() {
    NotesAppTheme {
        NotesApp()
    }
}