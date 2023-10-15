package com.rahim.utils.base

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity

open class BaseActivity:ComponentActivity() {
    fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}