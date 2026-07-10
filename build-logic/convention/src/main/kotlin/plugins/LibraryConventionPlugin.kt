package plugins

import androidGradle
import applyPlugins
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import configureKotlinAndroid
import configureMultiPlatform
import convention.configureFlavors
import org.gradle.api.Plugin
import org.gradle.api.Project
import versionCatalog

class LibraryConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      applyPlugins {
        listOf(
          versionCatalog.findPlugin("kotlin.multiplatform").get().get().pluginId,
          versionCatalog.findPlugin("android.kotlin.multiplatform.library").get()
            .get().pluginId,
        )
      }
      configureMultiPlatform()
    }
  }
}
