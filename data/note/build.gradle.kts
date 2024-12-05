plugins {
  libs.plugins.yadino.run {
    alias(android.library)
    alias(android.hilt)
  }
}

android {
  namespace = "com.rahim.yadino.note"
}
dependencies {
  implementation(project(":domain:note"))
  implementation(project(":domain:sharedPreferences"))
  implementation(project(":data:database"))
}
