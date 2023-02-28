package com.actiangent.note.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.actiangent.note.ui.screen.detailnote.DetailNoteScreen
import com.actiangent.note.ui.screen.home.HomeScreen

@Composable
fun NoteNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val actions = remember(navController) {
        NoteNavigationAction(navController)
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                modifier = modifier,
                navigateToDetailNote = { noteId -> actions.navigateToDetailNote(noteId) }
            )
        }
        composable(
            route = Screen.DetailNote.route,
            arguments = listOf(navArgument(NoteRouteArgs.ROUTE_DETAIL_ARG) {
                type = NavType.IntType
            })
        ) {
            DetailNoteScreen(
                modifier = modifier,
                navigateBack = { actions.navigateUp() }
            )
        }
    }
}