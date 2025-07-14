plugins {
  libs.plugins.yadino.run {
    alias(android.library)
  }
}

android {
  namespace = "com.rahim.yadino.home.domin"
}
dependencies {
  implementation(projects.core.base)
}
