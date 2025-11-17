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
  }
}

dependencies {
  compileOnly(libs.android.gradlePlugin)
  compileOnly(libs.android.tools.common)
  compileOnly(libs.compose.gradlePlugin)
  compileOnly(libs.firebase.crashlytics.gradlePlugin)
  compileOnly(libs.firebase.performance.gradlePlugin)
  compileOnly(libs.kotlin.gradlePlugin)
  compileOnly(libs.ksp.gradlePlugin)
  compileOnly(libs.room.gradlePlugin)
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
  }
}
