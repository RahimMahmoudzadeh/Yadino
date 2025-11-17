plugins {
  libs.plugins.run {
    alias(library)
  }
}

android {
  namespace = "com.rahim.yadino.home.domin"
}
dependencies {
  implementation(projects.core.base)
}
