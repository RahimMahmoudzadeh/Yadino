import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import config.Config
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import kotlin.apply

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(commonExtension: ApplicationExtension) {
  commonExtension.apply {
    compileSdk = Config.android.compileSdkVersion
    defaultConfig {
      minSdk = Config.android.minSdkVersion
    }
    compileOptions {
      sourceCompatibility = Config.jvm.javaVersion
      targetCompatibility = Config.jvm.javaVersion
      isCoreLibraryDesugaringEnabled = true
    }
    packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"

//    configure<KotlinAndroidProjectExtension> {
//
//      compilerOptions.apply {
//        val warningsAsErrors: String? by project
//        allWarningsAsErrors.set(warningsAsErrors.toBoolean())
//        jvmTarget.set(JvmTarget.JVM_17)
//        freeCompilerArgs.add("-Xexplicit-backing-fields")
//        freeCompilerArgs.addAll(Config.jvm.freeCompilerArgs)
//      }
//    }
  }

  dependencies {
    add("implementation", versionCatalog.findLibrary("androidx-core").get())
    add("implementation", versionCatalog.findLibrary("samanzamani").get())
    add("implementation", versionCatalog.findLibrary("timber").get())
    add("testImplementation", versionCatalog.findLibrary("junit").get())
    add("androidTestImplementation", versionCatalog.findBundle("test").get())
    add("coreLibraryDesugaring", versionCatalog.findLibrary("android.desugarJdkLibs").get())
  }
}

internal fun Project.configureComposeMultiPlatform() {
  extensions.configure<KotlinMultiplatformExtension> {

    targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
      .configureEach {
        compileSdk = Config.android.compileSdkVersion
        minSdk = Config.android.minSdkVersion
        val formattedPath = project.path.replace(":", ".").replace("-", "_")
        namespace = Config.android.nameSpace + formattedPath
        compilerOptions {
          jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
      }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
      commonMain.dependencies {
        val compose = versionCatalog.findBundle("compose").get()
        implementation(compose)
      }
      iosMain.dependencies {}

    }
  }
}

internal fun Project.configureKoin() {
  val koinBomProvider = versionCatalog.findLibrary("koin-bom").orElseThrow {
    NoSuchElementException("Koin BOM not found in catalog")
  }
  val koinBundleProvider = versionCatalog.findBundle("koin").orElseThrow {
    NoSuchElementException("Koin bundle not found in catalog")
  }
  extensions.configure<KotlinMultiplatformExtension> {
    sourceSets {
      commonMain.dependencies {
        implementation(project.dependencies.platform(koinBomProvider))
        implementation(koinBundleProvider)
      }
    }
  }
}

internal fun Project.configureMultiPlatform(commonMainDependency: KotlinDependencyHandler.() -> Unit = {}) {
  extensions.configure<KotlinMultiplatformExtension> {

    targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
      .configureEach {
        compileSdk = Config.android.compileSdkVersion
        minSdk = Config.android.minSdkVersion
        val formattedPath = project.path.replace(":", ".").replace("-", "_")
        namespace = Config.android.nameSpace + formattedPath
        compilerOptions {
          jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
      }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
      commonMain.dependencies {
        commonMainDependency()
      }
      iosMain.dependencies {}

    }
  }
}

internal fun Project.configureComposeMultiPlatformPresentation() {
  extensions.configure<KotlinMultiplatformExtension> {

    targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
      .configureEach {
        compileSdk = Config.android.compileSdkVersion
        minSdk = Config.android.minSdkVersion
        val formattedPath = project.path.replace(":", ".").replace("-", "_")
        namespace = Config.android.nameSpace + formattedPath
        compilerOptions {
          jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
      }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
      commonMain.dependencies {
        implementation(project(":library:designsystem"))
        implementation(project(":library:navigation"))
      }
      iosMain.dependencies {}
    }
  }
}
