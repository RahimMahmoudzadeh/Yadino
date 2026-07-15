package plugins

import applyPlugins
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import config.Config
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.Actions.with
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import versionCatalog

class CmpLibraryConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      applyPlugins {
        listOf(
          versionCatalog.findPlugin("kotlin.multiplatform").get().get().pluginId,
          versionCatalog.findPlugin("android.kotlin.multiplatform.library").get()
            .get().pluginId,
          versionCatalog.findPlugin("kotlinx.serialization").get().get().pluginId,
        )
      }

      extensions.configure<KotlinMultiplatformExtension> {

        targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
          .configureEach {
            compileSdk = Config.android.compileSdkVersion
            minSdk = Config.android.minSdkVersion
            namespace = Config.android.nameSpace + "shared"
            compilerOptions {
              jvmTarget.set(JvmTarget.JVM_17)
            }
          }

        iosArm64()
        iosSimulatorArm64()

        applyDefaultHierarchyTemplate()

        sourceSets.apply {
          commonMain.dependencies {
            val subprojects = project
              .rootProject
              .subprojects
            subprojects.filter { it.path.startsWith(":feature:", false) }
              .forEach { implementation(project(it.path)) }
            subprojects.filter { it.path.startsWith(":home:", false) }
              .forEach { implementation(project(it.path)) }
            subprojects.filter { it.path.startsWith(":onboarding:", false) }
              .forEach { implementation(project(it.path)) }
            subprojects.filter { it.path.startsWith(":routine:", false) }
              .forEach { implementation(project(it.path)) }
            subprojects.filter { it.path.startsWith(":note:", false) }
              .forEach { implementation(project(it.path)) }
            subprojects.filter { it.path.startsWith(":library:", false) }
              .forEach { implementation(project(it.path)) }
            subprojects.filter { it.path.startsWith(":core:", false) }
              .forEach { implementation(project(it.path)) }
            implementation(versionCatalog.findBundle("compose").get())
            implementation(versionCatalog.findLibrary("kotlinx-serialization").get())
          }
          androidMain.dependencies {
          }
        }
      }
    }
  }
}
