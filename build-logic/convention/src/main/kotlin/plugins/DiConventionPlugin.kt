package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import versionCatalog

class DiConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {

      dependencies {
        add("implementation", platform(versionCatalog.findLibrary("koin.bom").get()))
        add("implementation", versionCatalog.findBundle("koin").get())
      }
    }
  }
}
