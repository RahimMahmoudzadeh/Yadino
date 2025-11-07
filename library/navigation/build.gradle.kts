plugins {
  libs.plugins.run {
    yadino.run {
      alias(android.library)
      alias(android.library.compose)
    }
    alias(kotlinx.serialization)
  }
}

android {
  namespace = "com.rahim.yadino.library.navigation"
}
dependencies {
  implementation(projects.library.designsystem)
  libs.run {
    implementation(kotlinx.serialization)
  }
}
