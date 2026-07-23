import config.Config
import org.gradle.declarative.dsl.schema.FqName.Empty.packageName
import kotlin.text.replace
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
  libs.plugins.run {
    alias(library)
    alias(library.compose)
    alias(kotlinx.serialization)
    alias(buildkonfig)
  }
}
buildkonfig {
  val path=project.path.replace(":", ".").replace("-", "_")
  packageName = Config.android.applicationId+ Config.android.applicationIdSuffix + path

  defaultConfigs {
    buildConfigField(STRING, "APP_VERSION", Config.android.versionName)
  }
}
kotlin {
  sourceSets{
    commonMain.dependencies{
      projects.run{
        implementation(library.designsystem)
        implementation(core.ui)
      }
      libs.run{
        implementation(kotlinx.serialization)
      }
    }
  }
}
