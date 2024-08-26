package com.rahim.utils.permissionHandler

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher

interface PermissionHandlerNotification {
  fun permissionLauncher(
    activity: ComponentActivity,
    callback: (isGranted: Boolean) -> Unit,
  ): ActivityResultLauncher<String>

  fun checkPermissionNotification(
    activity: ComponentActivity,
    context: Context,
    callback: (isGranted: Boolean) -> Unit,
  )
}
