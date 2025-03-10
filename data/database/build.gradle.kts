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
  implementation(projects.data.note)
  implementation(projects.data.routine)
  implementation(projects.data.dateTime)
}
