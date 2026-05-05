import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(commonExtension: ApplicationExtension) {
  commonExtension.apply {
    buildFeatures {
      compose = true
    }

    dependencies {
      val bom = versionCatalog.findLibrary("androidx-compose-bom").get()
      val compose = versionCatalog.findBundle("compose").get()
      add("implementation", platform(bom))
      add("implementation", compose)
      add("androidTestImplementation", platform(bom))
      add("implementation", versionCatalog.findLibrary("ui-tooling").get())
    }

    testOptions {
      unitTests {
        // For Robolectric
        isIncludeAndroidResources = true
      }
    }
  }
}
internal fun Project.configureAndroidCompose(commonExtension: LibraryExtension) {
  commonExtension.apply {
    buildFeatures {
      compose = true
    }

    dependencies {
      val bom = versionCatalog.findLibrary("androidx-compose-bom").get()
      val compose = versionCatalog.findBundle("compose").get()
      add("implementation", platform(bom))
      add("implementation", compose)
      add("androidTestImplementation", platform(bom))
      add("implementation", versionCatalog.findLibrary("ui-tooling").get())
    }

    testOptions {
      unitTests {
        // For Robolectric
        isIncludeAndroidResources = true
      }
    }
  }
}
