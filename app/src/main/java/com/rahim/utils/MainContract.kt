package com.rahim.utils

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalViewModel
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.rahim.yadino.navigation.component.DrawerItemType
import com.rahim.yadino.note.model.NoteModel

interface MainContract : UnidirectionalViewModel<MainContract.MainEvent, MainContract.MainState> {

  @Immutable
  sealed interface MainEvent {
    data object CheckedAllRoutinePastTime : MainEvent
    data class ClickDrawer(val drawerItemType: DrawerItemType) : MainEvent
  }

  @Immutable
  data class MainState(
    val isLoading: Boolean = false,
    val isDarkTheme: Boolean? = false,
    val haveAlarm: Boolean = false,
    val isShowWelcomeScreen: Boolean = true,
  )
}
