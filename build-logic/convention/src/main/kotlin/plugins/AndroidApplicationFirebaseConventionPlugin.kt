package plugins


import androidGradle
import applyPlugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import versionCatalog

class AndroidApplicationFirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins {
                listOf(
                    "com.google.gms.google-services",
                    "com.google.firebase.crashlytics"
                )
            }
            applyDependencies()
        }
    }

    private fun Project.applyDependencies() {
        dependencies {
            val bom = versionCatalog.findLibrary("firebaseBom").get()
            add("implementation", platform(bom))
            add("implementation", versionCatalog.findBundle("firebase").get())
        }
    }
}

