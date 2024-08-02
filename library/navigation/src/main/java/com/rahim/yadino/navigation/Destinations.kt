package com.rahim.yadino.navigation

sealed class Destinations(val route:String) {

    data object Home : Destinations(ModuleRoutes.HOME.route)
    data object Routine : Destinations(ModuleRoutes.ROUTINE.route)
    data object Note : Destinations(ModuleRoutes.NOTE.route)
    data object Welcome : Destinations(ModuleRoutes.WELCOME.route)
    data object AlarmHistory : Destinations(ModuleRoutes.HISTORY.route)
    data object Calender : Destinations(ModuleRoutes.CALENDER.route)

}
private enum class ModuleRoutes(val route: String) {
    HOME("home"),
    ROUTINE("routine"),
    NOTE("note"),
    CALENDER("calendar"),
    WELCOME("welcome"),
    HISTORY("history"),
    EMPTY("empty"),
}