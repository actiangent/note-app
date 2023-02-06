package com.actiangent.note.ui.navigation

const val ROUTE_HOME = "home"
const val ROUTE_DETAIL = "note/"
const val ROUTE_DETAIL_ARG = "noteId"

sealed class Screen(val route: String) {
    object Home : Screen(ROUTE_HOME)
    object DetailNote : Screen("$ROUTE_DETAIL{$ROUTE_DETAIL_ARG}") {
        fun createDetailNoteRoute(noteId: Int) = "$ROUTE_DETAIL$noteId"
    }
}