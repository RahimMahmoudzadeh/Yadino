plugins {
  libs.plugins.yadino.run {
    alias(android.feature)
    alias(android.library.compose)
  }
}

android {
  namespace = "com.rahim.yadino.feature.calender"
}
dependencies {
  libs.run {
    implementation(bundles.vico)
  }
  projects.run{
    implementation(domain.sharedPreferences)
    implementation(domain.dateTime)
  }
}
