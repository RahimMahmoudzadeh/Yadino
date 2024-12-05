plugins {
  libs.plugins.yadino.run {
    alias(android.library)
    alias(android.hilt)
  }
}

android {
  namespace = "com.rahim.yadino.dateTime"
}
dependencies {
  implementation(project(":domain:dateTime"))
  implementation(project(":core:base"))
  implementation(project(":data:database"))
}
