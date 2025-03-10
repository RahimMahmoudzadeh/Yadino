plugins {
  libs.plugins.yadino.run {
    alias(android.library)
    alias(android.hilt)
    alias(android.room)
  }
}

android {
  namespace = "com.rahim.yadino.note"
}
dependencies {
  projects.run{
    implementation(domain.note)
    implementation(domain.sharedPreferences)
    implementation(data.database)
  }
}
