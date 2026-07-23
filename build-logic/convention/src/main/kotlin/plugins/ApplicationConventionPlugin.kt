package plugins

import applicationGradle
import applyPlugins
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.LibraryExtension
import config.Config
import configureKotlinAndroid
import convention.configureFlavors
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import versionCatalog

class ApplicationConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      applyPlugins {
        listOf(
          "com.android.application",
          versionCatalog.findPlugin("kotlin-parcelize").get().get().pluginId,
        )
      }
      applicationGradle {
        defaultConfig.apply {
          targetSdk = Config.android.targetSdkVersion
          applicationId = Config.android.applicationId
          versionCode = Config.android.versionCode
          versionName = Config.android.versionName
          namespace = Config.android.applicationId
        }
        configureKotlinAndroid(this)
        configureFlavors(this)
      }
      dependencies {


      }
    }
  }
}
