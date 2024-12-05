plugins {
  libs.plugins.yadino.run {
    alias(android.library)
    alias(android.hilt)
  }
}

android {
  namespace = "com.rahim.yadino.routine"
}
dependencies {
  implementation(project(":domain:routine"))
  implementation(project(":domain:sharedPreferences"))
  implementation(project(":data:database"))
  implementation(project(":core:base"))
}
