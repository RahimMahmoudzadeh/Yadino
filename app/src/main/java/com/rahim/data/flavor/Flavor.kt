package com.rahim.data.flavor

sealed interface DrawerItemType {
  data object ShareWithFriends : DrawerItemType
  data object RateToApp : DrawerItemType
  data object ChangeTheme : DrawerItemType
}

interface Flavor {
  fun drawerItemType(drawerItemType:DrawerItemType)
}
