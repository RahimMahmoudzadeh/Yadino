plugins {
  libs.plugins.yadino.run {
    alias(android.library)
    alias(android.hilt)
    alias (android.room)
  }
}

android {
  namespace = "com.rahim.yadino.dateTime"
}
dependencies {
  projects.run {
    implementation(domain.dateTime)
    implementation(core.base)
  }
}
