package com.rahim.data.flavor

sealed interface DrawerItemType {
  data object ShareWithFriends : DrawerItemType
  data object RateToApp : DrawerItemType
}

interface Flavor {
  fun drawerItemType(drawerItemType:DrawerItemType)
}
