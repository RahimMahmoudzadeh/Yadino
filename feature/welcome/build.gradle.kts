plugins {
  libs.plugins.yadino.run {
    alias(android.feature)
    alias(android.library.compose)
    alias(android.hilt)
  }
}

android {
  namespace = "com.rahim.yadino.feature.welcome"
}
dependencies {
  implementation(projects.domain.sharedPreferences)
  libs.run {
    implementation(bundles.accompanist)
  }
}
