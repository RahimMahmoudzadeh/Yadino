package plugins
import androidGradle
import applyPlugins
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project

class LibraryComposeConventionPlugin : Plugin<Project> {
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
