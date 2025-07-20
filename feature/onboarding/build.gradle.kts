plugins {
  libs.plugins.yadino.run {
    alias(android.feature)
    alias(android.library.compose)
    alias(android.hilt)
  }
}

android {
  namespace = "com.rahim.yadino.feature.onboarding"
}
dependencies {
  libs.run {
    implementation(bundles.accompanist)
  }
}
