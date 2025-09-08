package com.rahim.data.distributionActions

sealed interface DrawerItemType {
  data object ShareWithFriends : DrawerItemType
  data object RateToApp : DrawerItemType
}

sealed interface StateOfClickItemDrawable {
  data object InstallApp : StateOfClickItemDrawable
  data object IntentSuccess : StateOfClickItemDrawable
}

interface AppDistributionActions {
  fun drawerItemType(drawerItemType: DrawerItemType): StateOfClickItemDrawable
}
