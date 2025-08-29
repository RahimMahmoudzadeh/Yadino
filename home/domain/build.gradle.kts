plugins {
  libs.plugins.yadino.run {
    alias(android.library)
    alias(android.hilt)
  }
}

android {
  namespace = "com.rahim.yadino.home.domin"
}
dependencies {
  implementation(projects.core.base)
}
