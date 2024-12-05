plugins {
  libs.plugins.yadino.run {
    alias(android.feature)
    alias(android.library.compose)
  }
}

android {
  namespace = "com.rahim.yadino.feature.calender"
}
dependencies {
  libs.run {
    implementation(bundles.vico)
  }
  implementation(project(":domain:sharedPreferences"))
  implementation(project(":domain:dateTime"))
}
