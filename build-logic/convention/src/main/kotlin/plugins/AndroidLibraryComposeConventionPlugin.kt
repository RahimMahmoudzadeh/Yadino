package plugins
import androidGradle
import applyPlugins
import configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      applyPlugins {
        listOf("com.android.library", "org.jetbrains.kotlin.plugin.compose")
      }
      androidGradle {
        configureAndroidCompose(this)
      }
    }
  }
}
