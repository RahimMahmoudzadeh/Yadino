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
    register("androidApplicationCompose") {
      id = "yadino.android.application.compose"
      implementationClass = "plugins.AndroidApplicationComposeConventionPlugin"
    }
    register("androidApplication") {
      id = "yadino.android.application"
      implementationClass = "plugins.AndroidApplicationConventionPlugin"
    }
    register("androidLibraryCompose") {
      id = "yadino.android.library.compose"
      implementationClass = "plugins.AndroidLibraryComposeConventionPlugin"
    }
    register("androidLibrary") {
      id = "yadino.android.library"
      implementationClass = "plugins.AndroidLibraryConventionPlugin"
    }
    register("androidFeature") {
      id = "yadino.android.feature"
      implementationClass = "plugins.AndroidFeatureConventionPlugin"
    }
    register("androidHilt") {
      id = "yadino.android.hilt"
      implementationClass = "plugins.AndroidHiltConventionPlugin"
    }
    register("androidRoom") {
      id = "yadino.android.room"
      implementationClass = "plugins.AndroidRoomConventionPlugin"
    }
    register("androidFirebase") {
      id = "yadino.android.application.firebase"
      implementationClass = "plugins.AndroidApplicationFirebaseConventionPlugin"
    }
  }
}
