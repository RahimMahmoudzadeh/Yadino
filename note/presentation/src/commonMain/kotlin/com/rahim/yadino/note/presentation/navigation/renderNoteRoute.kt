package com.rahim.yadino.note.presentation.navigation

import androidx.navigation3.runtime.NavEntry
import com.rahim.yadino.navigation.component.base.NavRoute

fun renderNoteRoute(
  route: NavRoute.Note,
  onNavigate: (NavRoute) -> Unit,
) : NavEntry<NavRoute> {
  return NavEntry(key = route) {
    when (route) {
      is NavRoute.Note.Main -> {
      }
    }
  }
}
