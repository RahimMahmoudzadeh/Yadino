plugins {
  libs.plugins.run {
    alias(library)
    alias(library.compose)
  }
}

android {
  namespace = "com.rahim.yadino.library.designsystem"
}
dependencies {
  implementation(projects.core.base)
  libs.run {
    implementation(accompanist.permissions)
    implementation(swipe)
    api(kotlinx.collections.immutable)
  }
}
