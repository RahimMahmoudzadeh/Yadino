package plugins

import androidGradle
import applyPlugins
import configureKotlinAndroid
import convention.configureFlavors
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins { listOf("com.android.library","org.jetbrains.kotlin.android") }
            androidGradle{
                configureKotlinAndroid(this)
                configureFlavors(this)
            }
        }
    }
}
