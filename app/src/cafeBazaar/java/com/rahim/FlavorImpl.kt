package com.rahim

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.rahim.data.flavor.DrawerItemType
import com.rahim.data.flavor.Flavor
import com.rahim.yadino.Constants.CAFE_BAZAAR_PACKAGE_NAME
import com.rahim.yadino.Constants.CAFE_BAZZAR_LINK
import com.rahim.yadino.isPackageInstalled
import javax.inject.Inject

class FlavorImpl @Inject constructor(private val context: Context): Flavor {
  override fun drawerItemType(drawerItemType: DrawerItemType) {
    when(drawerItemType){
      DrawerItemType.RateToApp -> {
        if (!CAFE_BAZAAR_PACKAGE_NAME.isPackageInstalled(
            context.packageManager,
          )
        ) {
          Toast.makeText(
            context,
            context.resources.getString(com.rahim.R.string.install_cafeBazaar),
            Toast.LENGTH_SHORT,
          ).show()
          return
        }
        val intent = Intent(Intent.ACTION_EDIT)
        intent.setData(Uri.parse("bazaar://details?id=${context.packageName}"))
        intent.setPackage(CAFE_BAZAAR_PACKAGE_NAME)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent, null)
      }
      DrawerItemType.ShareWithFriends ->{
        val sendIntent: Intent = Intent().apply {
          action = Intent.ACTION_SEND
          putExtra(Intent.EXTRA_TEXT, CAFE_BAZZAR_LINK)
          type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent, null)
      }
    }
  }
}
