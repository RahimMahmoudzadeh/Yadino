package plugins

import configureComposeMultiPlatform
import configureKoin
import configureMultiPlatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import versionCatalog

class DiConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      configureKoin()
    }
  }
}
