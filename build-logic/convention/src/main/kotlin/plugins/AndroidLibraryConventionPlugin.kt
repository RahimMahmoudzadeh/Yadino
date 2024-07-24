package plugins

import androidGradle
import applyPlugins
import configureKotlinAndroid
import convention.configureFlavors
import org.gradle.api.Plugin
import org.gradle.api.Project
import versionCatalog

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins {
                listOf(
                    versionCatalog.findPlugin("com.android.library").get().get().pluginId,
                    versionCatalog.findPlugin("kotlinAndroid").get().get().pluginId,
                    versionCatalog.findPlugin("kotlin-parcelize").get().get().pluginId
                )
            }
            androidGradle {
                configureKotlinAndroid(this)
                configureFlavors(this)
            }
        }
    }
}
