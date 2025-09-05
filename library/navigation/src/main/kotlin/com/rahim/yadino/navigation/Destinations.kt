package com.rahim.yadino.navigation

sealed class Destinations(val route: String) {

  data object Home : Destinations(ModuleRoutes.HOME.route)
  data object Routine : Destinations(ModuleRoutes.ROUTINE.route)
  data object Note : Destinations(ModuleRoutes.NOTE.route)
  data object OnBoarding : Destinations(ModuleRoutes.OnBoarding.route)
  data object AlarmHistory : Destinations(ModuleRoutes.HISTORY.route)
  data object Calender : Destinations(ModuleRoutes.CALENDER.route)
  data object Empty : Destinations(ModuleRoutes.EMPTY.route)
}
private enum class ModuleRoutes(val route: String) {
  HOME("home"),
  ROUTINE("routine"),
  NOTE("note"),
  CALENDER("calendar"),
  OnBoarding("onboarding"),
  HISTORY("history"),
  EMPTY("empty"),
}
