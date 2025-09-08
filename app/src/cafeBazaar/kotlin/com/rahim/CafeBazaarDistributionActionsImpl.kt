package com.rahim

import android.content.Context
import android.content.Intent
import com.rahim.data.distributionActions.DrawerItemType
import com.rahim.data.distributionActions.AppDistributionActions
import com.rahim.data.distributionActions.StateOfClickItemDrawable
import com.rahim.yadino.Constants.CAFE_BAZAAR_PACKAGE_NAME
import com.rahim.yadino.Constants.CAFE_BAZZAR_LINK
import com.rahim.yadino.isPackageInstalled
import javax.inject.Inject
import androidx.core.net.toUri

class CafeBazaarDistributionActionsImpl @Inject constructor(private val context: Context) : AppDistributionActions {
  override fun drawerItemType(drawerItemType: DrawerItemType): StateOfClickItemDrawable {
    return when (drawerItemType) {
      DrawerItemType.RateToApp -> {
        if (!CAFE_BAZAAR_PACKAGE_NAME.isPackageInstalled(
            context.packageManager,
          )
        ) {
          return StateOfClickItemDrawable.InstallApp
        }
        val intent = Intent(Intent.ACTION_EDIT)
        intent.data = "bazaar://details?id=${context.packageName}".toUri()
        intent.setPackage(CAFE_BAZAAR_PACKAGE_NAME)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent, null)
        StateOfClickItemDrawable.IntentSuccess
      }
      DrawerItemType.ShareWithFriends -> {
        val sendIntent: Intent = Intent().apply {
          action = Intent.ACTION_SEND
          putExtra(Intent.EXTRA_TEXT, CAFE_BAZZAR_LINK)
          type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent, null)
        StateOfClickItemDrawable.IntentSuccess
      }
    }
  }
}
