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
  projects.run {
    implementation(domain.routine)
    implementation(domain.sharedPreferences)
    implementation(data.database)
    implementation(core.base)
  }
}
