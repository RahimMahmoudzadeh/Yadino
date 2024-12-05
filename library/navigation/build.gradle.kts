plugins {
  libs.plugins.yadino.run {
    alias(android.library)
    alias(android.library.compose)
  }
}

android {
  namespace = "com.rahim.yadino.library.navigation"
}
dependencies {
  implementation(projects.library.designsystem)
}
