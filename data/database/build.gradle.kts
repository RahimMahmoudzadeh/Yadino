plugins {
  libs.plugins.yadino.run {
    alias(android.library)
    alias(android.hilt)
    alias(android.room)
  }
}

android {
  namespace = "com.rahim.yadino.data.database"
}

dependencies {
  implementation(project(":domain:note"))
  implementation(project(":domain:dateTime"))
  implementation(project(":domain:routine"))
  implementation(project(":domain:sharedPreferences"))
}
