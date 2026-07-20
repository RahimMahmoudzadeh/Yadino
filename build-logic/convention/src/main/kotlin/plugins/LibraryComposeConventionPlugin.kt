package plugins

import androidGradle
import applyPlugins
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import configureAndroidCompose
import configureComposeMultiPlatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import versionCatalog

class LibraryComposeConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      applyPlugins {
        listOf(
          versionCatalog.findPlugin("composeMultiplatform").get().get().pluginId,
          versionCatalog.findPlugin("compose.compiler").get().get().pluginId,
        )
      }
      configureComposeMultiPlatform()
    }
  }
}
