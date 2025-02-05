package plugins

import applicationGradle
import applyPlugins
import configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      applyPlugins {
        listOf("com.android.application", "org.jetbrains.kotlin.plugin.compose")
      }
      applicationGradle {
        configureAndroidCompose(this)
      }
    }
  }
}
