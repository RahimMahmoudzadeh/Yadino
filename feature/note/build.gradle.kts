plugins {
  libs.plugins.yadino.run {
    alias(android.feature)
    alias(android.library.compose)
  }
}

android {
  namespace = "com.rahim.yadino.feature.note"
}
dependencies {
  projects.run {
    implementation(domain.dateTime)
  }
}
