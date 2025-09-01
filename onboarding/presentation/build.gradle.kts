plugins {
  libs.plugins.yadino.run {
    alias(android.feature)
    alias(android.library.compose)
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
