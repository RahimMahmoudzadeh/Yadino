package com.rahim.data.flavor

sealed interface DrawerItemType {
  data object ShareWithFriends : DrawerItemType
  data object RateToApp : DrawerItemType
}

sealed interface StateOfClickItemDrawable {
  data object InstallApp : StateOfClickItemDrawable
  data object IntentSuccess : StateOfClickItemDrawable
}


interface Flavor {
  fun drawerItemType(drawerItemType:DrawerItemType):StateOfClickItemDrawable
}
