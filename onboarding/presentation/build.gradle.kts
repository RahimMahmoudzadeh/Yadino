plugins {
  libs.plugins.run {
    alias(presentation)
    alias(library.compose)
    alias(decompose)
  }
}

android {
  namespace = "com.rahim.yadino.onboarding.presentation"
}
dependencies {
  libs.run {
    implementation(bundles.accompanist)
  }
}
