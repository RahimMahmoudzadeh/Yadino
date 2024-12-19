package com.rahim

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.rahim.data.flavor.DrawerItemType
import com.rahim.data.flavor.Flavor
import com.rahim.data.flavor.StateOfClickItemDrawable
import com.rahim.yadino.Constants.MY_KET_LINK
import com.rahim.yadino.Constants.MY_KET_PACKAGE_NAME
import com.rahim.yadino.isPackageInstalled
import javax.inject.Inject

class FlavorImpl @Inject constructor(private val context: Context) : Flavor {
  override fun drawerItemType(drawerItemType: DrawerItemType): StateOfClickItemDrawable {
    return when (drawerItemType) {
      DrawerItemType.RateToApp -> {
        if (!MY_KET_PACKAGE_NAME.isPackageInstalled(
            context.packageManager,
          )
        ) {
          return StateOfClickItemDrawable.InstallApp
        }
        val url = "myket://comment?id=${context.packageName}"
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setAction(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        context.startActivity(intent)
        StateOfClickItemDrawable.IntentSuccess
      }

      DrawerItemType.ShareWithFriends -> {
        val sendIntent: Intent = Intent().apply {
          action = Intent.ACTION_SEND
          putExtra(Intent.EXTRA_TEXT, MY_KET_LINK)
          type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
        StateOfClickItemDrawable.IntentSuccess
      }
    }
  }
}
