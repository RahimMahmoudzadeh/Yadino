plugins {
  libs.plugins.yadino.run {
    alias(android.library)
    alias(android.hilt)
  }
}

android {
  namespace = "com.rahim.yadino.data.sharedPreferences"
}
dependencies {
  implementation(project(":domain:sharedPreferences"))
  implementation(project(":core:base"))
  implementation(project(":data:database"))
}
