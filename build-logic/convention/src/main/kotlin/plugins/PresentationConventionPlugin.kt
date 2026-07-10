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
        listOf("library", "di")
      }
      configureComposeMultiPlatformPresentation()
    }
  }
}
