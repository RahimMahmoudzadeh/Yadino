package plugins

import applicationGradle
import applyPlugins
import com.android.build.api.dsl.ApplicationExtension
import configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType


class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins {
                listOf("com.android.application", "org.jetbrains.kotlin.plugin.compose")
            }
            applicationGradle {
                configureAndroidCompose(this)
            }
        }
    }

}
