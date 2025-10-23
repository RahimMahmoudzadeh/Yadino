package plugins

import applyPlugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import versionCatalog

class AndroidFeatureConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      applyPlugins {
        listOf("yadino.android.library", "yadino.di")
      }
      dependencies {
        add("implementation", versionCatalog.findLibrary("androidx.tracing.ktx").get())
        add("api", project(":library:designsystem"))
        add("api", project(":library:navigation"))
        add("api", project(":core:base"))
      }
    }
  }
}
