package com.actiangent.note.ui.navigation

import androidx.navigation.NavController
import com.actiangent.note.ui.navigation.NoteRoute.ROUTE_DETAIL
import com.actiangent.note.ui.navigation.NoteRoute.ROUTE_HOME
import com.actiangent.note.ui.navigation.NoteRouteArgs.ROUTE_DETAIL_ARG

object NoteRoute {
    const val ROUTE_HOME = "home"
    const val ROUTE_DETAIL = "note"
}

object NoteRouteArgs {
    const val ROUTE_DETAIL_ARG = "noteId"
}

sealed class Screen(val route: String) {
    object Home : Screen(ROUTE_HOME)
    object DetailNote : Screen("$ROUTE_DETAIL/{$ROUTE_DETAIL_ARG}") {
        fun createDetailNoteRoute(noteId: Int) = "$ROUTE_DETAIL/$noteId"
    }
}

class NoteNavigationAction(private val navController: NavController) {
    fun navigateToDetailNote(noteId: Int) {
        navController.navigate(Screen.DetailNote.createDetailNoteRoute(noteId))
    }

    fun navigateUp() {
        navController.navigateUp()
    }
}