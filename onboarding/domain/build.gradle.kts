plugins {
  libs.plugins.run {
    alias(library)
  }
}

android {
  namespace = "com.rahim.yadino.onBoarding.domain"
}
dependencies {
  implementation(projects.core.base)
}
