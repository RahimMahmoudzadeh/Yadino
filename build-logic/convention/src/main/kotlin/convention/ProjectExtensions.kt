import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

val Project.versionCatalog
  get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

inline fun Project.androidGradle(crossinline configure: LibraryExtension.() -> Unit) = extensions.configure<LibraryExtension> {
  configure()
}

inline fun Project.applicationGradle(crossinline configure: ApplicationExtension.() -> Unit) = extensions.configure<ApplicationExtension> {
  configure()
}

inline fun Project.applyPlugins(crossinline plugin: () -> List<String>) {
  pluginManager.apply {
    plugin().forEach {
      apply(plugin = it)
    }
  }
}
