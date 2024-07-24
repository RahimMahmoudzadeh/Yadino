package plugins
import applyPlugins
import com.google.devtools.ksp.gradle.KspExtension
import versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.process.CommandLineArgumentProvider
import java.io.File

class AndroidRoomConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            applyPlugins {
                listOf("com.google.devtools.ksp")
            }
            extensions.configure<KspExtension> {
                arg(RoomSchemaArgProvider(File(projectDir, "schemas")))
            }

            dependencies {
                add("implementation", versionCatalog.findBundle("room").get())
                add("ksp", versionCatalog.findLibrary("room.compiler").get())
            }
        }
    }

    class RoomSchemaArgProvider(
        @get:InputDirectory
        @get:PathSensitive(PathSensitivity.RELATIVE)
        val schemaDir: File,
    ) : CommandLineArgumentProvider {
        override fun asArguments() = listOf("room.schemaLocation=${schemaDir.path}")
    }
}