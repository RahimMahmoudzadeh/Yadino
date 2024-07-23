package plugins
import androidGradle
import applyPlugins
import com.android.build.gradle.LibraryExtension
import configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins {
                listOf("com.android.library", "org.jetbrains.kotlin.plugin.compose")
            }
            androidGradle{
                configureAndroidCompose(this)
            }
        }
    }

}
