plugins {
  libs.plugins.yadino.run {
    alias(android.feature)
    alias(android.library.compose)
  }
}

android {
  namespace = "com.rahim.yadino.feature.routine"
}
dependencies {
  implementation(project(":data:routine"))
  implementation(project(":data:dateTime"))
  implementation(project(":data:sharedPreferences"))

  libs.run {
    implementation(bundles.accompanist)
    implementation(androidx.core.splashscreen)
    implementation(datetime)
  }
}
