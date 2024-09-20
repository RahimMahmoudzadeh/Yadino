import com.android.build.api.dsl.CommonExtension
import config.Config
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.provideDelegate

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
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
        kotlinOptions {
            val warningsAsErrors: String? by project
            allWarningsAsErrors = warningsAsErrors.toBoolean()
            jvmTarget = Config.jvm.kotlinJvm
            freeCompilerArgs = freeCompilerArgs + Config.jvm.freeCompilerArgs
        }
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
