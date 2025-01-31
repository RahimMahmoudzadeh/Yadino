package plugins

import androidGradle
import applyPlugins
import versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins {
                listOf("yadino.android.library", "yadino.android.hilt")
            }
            dependencies {
                add("implementation", versionCatalog.findLibrary("androidx.tracing.ktx").get())
                add(
                    "implementation",
                    versionCatalog.findLibrary("hilt.navigation.compose").get()
                )
                add("api", project(":library:designsystem"))
                add("api", project(":library:navigation"))
                add("api", project(":core:base"))
            }
        }
    }
}
