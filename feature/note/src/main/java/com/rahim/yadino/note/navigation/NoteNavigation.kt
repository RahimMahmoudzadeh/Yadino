package com.rahim.yadino.note.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rahim.yadino.navigation.Destinations
import com.rahim.yadino.note.NoteRoute

fun NavController.navigateToNote(navOptions: NavOptions? = null) {
    this.navigate(Destinations.Note.route, navOptions)
}

fun NavGraphBuilder.noteScreen(
    openDialog: Boolean,
    clickSearch: Boolean,
    onOpenDialog: (isOpen: Boolean) -> Unit
) {
    composable(Destinations.Note.route) {
        NoteRoute(openDialog = openDialog, clickSearch = clickSearch, onOpenDialog = onOpenDialog)
    }
}