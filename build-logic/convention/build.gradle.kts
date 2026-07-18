import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
  `kotlin-dsl`
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
  compilerOptions {
    jvmTarget = JvmTarget.JVM_17
    freeCompilerArgs.addAll(listOf("-opt-in=kotlin.RequiresOptIn"))
  }
}

dependencies {
  implementation(libs.android.gradlePlugin)
  implementation(libs.android.tools.common)
  implementation(libs.compose.gradlePlugin)
  implementation(libs.firebase.crashlytics.gradlePlugin)
  implementation(libs.firebase.performance.gradlePlugin)
  implementation(libs.kotlin.gradlePlugin)
  implementation(libs.ksp.gradlePlugin)
  implementation(libs.room.gradlePlugin)
  implementation(libs.compose.multiplatform.gradlePlugin)
}

gradlePlugin {
  plugins {
    register("applicationCompose") {
      id = "application.compose"
      implementationClass = "plugins.ApplicationComposeConventionPlugin"
    }
    register("application") {
      id = "yadino.application"
      implementationClass = "plugins.ApplicationConventionPlugin"
    }
    register("libraryCompose") {
      id = "library.compose"
      implementationClass = "plugins.LibraryComposeConventionPlugin"
    }
    register("library") {
      id = "library"
      implementationClass = "plugins.LibraryConventionPlugin"
    }
    register("presentation") {
      id = "presentation.module"
      implementationClass = "plugins.PresentationConventionPlugin"
    }
    register("di") {
      id = "di"
      implementationClass = "plugins.DiConventionPlugin"
    }
    register("db") {
      id = "db"
      implementationClass = "plugins.DbConventionPlugin"
    }
    register("firebase") {
      id = "firebase"
      implementationClass = "plugins.FirebaseConventionPlugin"
    }
    register("decompose") {
      id = "decompose"
      implementationClass = "plugins.DecomposeConventionPlugin"
    }
    register("cmp") {
      id = "cmp"
      implementationClass = "plugins.CmpLibraryConventionPlugin"
    }
  }
}
