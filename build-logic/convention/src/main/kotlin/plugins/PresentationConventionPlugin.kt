package plugins

import applyPlugins
import configureComposeMultiPlatformPresentation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import versionCatalog

class PresentationConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      applyPlugins {
        listOf(
          versionCatalog.findPlugin("kotlin-multiplatform").get().get().pluginId,
          versionCatalog.findPlugin("android-kotlin-multiplatform-library").get().get().pluginId,
          versionCatalog.findPlugin("composeMultiplatform").get().get().pluginId,
          versionCatalog.findPlugin("compose-compiler").get().get().pluginId
        )
      }
      configureComposeMultiPlatformPresentation()
    }
  }
}
