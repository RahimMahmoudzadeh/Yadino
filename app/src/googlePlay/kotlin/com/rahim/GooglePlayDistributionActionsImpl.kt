package com.rahim

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.rahim.data.distributionActions.AppDistributionActions
import com.rahim.data.distributionActions.DrawerItemType
import com.rahim.data.distributionActions.StateOfClickItemDrawable
import com.rahim.yadino.Constants.GOOGLE_PLAY_LINK

class GooglePlayDistributionActionsImpl(private val context: Context) : AppDistributionActions {
  override fun drawerItemType(drawerItemType: DrawerItemType): StateOfClickItemDrawable {
    return when (drawerItemType) {
      DrawerItemType.RateToApp -> {
        try {
          val uri = Uri.parse("market://details?id=${context.packageName}")
          val intent = Intent(Intent.ACTION_VIEW, uri)
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
          context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
          val webUri = Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
          val webIntent = Intent(Intent.ACTION_VIEW, webUri)
          webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
          context.startActivity(webIntent)
        }
        StateOfClickItemDrawable.IntentSuccess
      }

      DrawerItemType.ShareWithFriends -> {
        val sendIntent: Intent = Intent().apply {
          action = Intent.ACTION_SEND
          putExtra(Intent.EXTRA_TEXT, GOOGLE_PLAY_LINK)
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
