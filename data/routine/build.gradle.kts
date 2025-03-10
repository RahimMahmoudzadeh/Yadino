plugins {
  libs.plugins.yadino.run {
    alias(android.library)
    alias(android.hilt)
    alias(android.room)
  }
}

android {
  namespace = "com.rahim.yadino.routine"
}
dependencies {
  projects.run {
    implementation(domain.routine)
    implementation(domain.sharedPreferences)
    implementation(core.base)
  }
}
