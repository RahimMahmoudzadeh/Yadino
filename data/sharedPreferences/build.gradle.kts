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
  projects.run {
    implementation(domain.sharedPreferences)
    implementation(core.base)
    implementation(data.database)
  }
}
