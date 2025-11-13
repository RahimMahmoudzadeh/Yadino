plugins {
  libs.plugins.yadino.run {
    alias(android.feature)
    alias(android.library.compose)
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
