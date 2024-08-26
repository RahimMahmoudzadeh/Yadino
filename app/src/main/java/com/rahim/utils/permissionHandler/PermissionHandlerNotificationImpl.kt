package com.rahim.utils.permissionHandler

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import javax.inject.Inject

class PermissionHandlerNotificationImpl @Inject constructor() :
  PermissionHandlerNotification {
  override fun permissionLauncher(
    activity: ComponentActivity,
    callback: (isGranted: Boolean) -> Unit,
  ): ActivityResultLauncher<String> {
    return activity.registerForActivityResult(
      ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
      callback(isGranted)
    }
  }

  override fun checkPermissionNotification(
    activity: ComponentActivity,
    context: Context,
    callback: (isGranted: Boolean) -> Unit,
  ) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      when {
        ContextCompat.checkSelfPermission(
          context,
          Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED -> {
          callback(true)
        }

        shouldShowRequestPermissionRationale(
          activity,
          Manifest.permission.POST_NOTIFICATIONS,
        ) -> {
          callback(false)
        }

        else -> {
          permissionLauncher(activity) { callback(it) }.launch(
            Manifest.permission.POST_NOTIFICATIONS,
          )
        }
      }
    } else {
      callback(true)
    }
  }
}
