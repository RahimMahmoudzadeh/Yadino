package plugins

import androidGradle
import applyPlugins
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import configureKotlinAndroid
import convention.configureFlavors
import org.gradle.api.Plugin
import org.gradle.api.Project
import versionCatalog

class LibraryConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      applyPlugins {
        listOf(
          versionCatalog.findPlugin("com.android.library").get().get().pluginId,
          versionCatalog.findPlugin("kotlin-parcelize").get().get().pluginId,
        )
      }
      androidGradle {
        configureKotlinAndroid(this)
        configureFlavors(this)
      }
    }
  }
}
