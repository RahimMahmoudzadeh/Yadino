package plugins

import applicationGradle
import applyPlugins
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import config.Config
import configureKotlinAndroid
import convention.configureFlavors
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import versionCatalog
import java.util.regex.Pattern.compile

class AndroidApplicationConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      applyPlugins {
        listOf("com.android.application", "org.jetbrains.kotlin.android")
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
        add(
          "implementation",
          versionCatalog.findLibrary("hilt.navigation.compose").get(),
        )
        val subprojects = project
          .rootProject
          .subprojects

        subprojects.filter { it.path.startsWith(":domain:", false) }
          .forEach { add("implementation", project(it.path)) }

        subprojects.filter { it.path.startsWith(":data:", false) }
          .forEach { add("implementation", project(it.path)) }

        subprojects.filter { it.path.startsWith(":feature:", false) }
          .forEach { add("implementation", project(it.path)) }
      }
    }
  }
}
