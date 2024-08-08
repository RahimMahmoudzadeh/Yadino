package plugins

import applyPlugins
import com.android.utils.TraceUtils.simpleId
import versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins {
                listOf(
                    "dagger.hilt.android.plugin",
                    versionCatalog.findPlugin("ksp").get().get().pluginId
                )
            }

            dependencies {
                add("implementation", versionCatalog.findLibrary("hilt.android").get())
                add("ksp", versionCatalog.findLibrary("hilt.compiler").get())
            }
        }
    }

}
