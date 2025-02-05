import config.Config

plugins {
  libs.plugins.yadino.run {
    alias(android.feature)
    alias(android.library.compose)
  }
}

android {
  namespace = "com.rahim.yadino.feature.routine"
}
dependencies {
  projects.run{
    implementation(domain.dateTime)
    implementation(domain.sharedPreferences)
    implementation(domain.routine)
  }

  libs.run {
    implementation(bundles.accompanist)
    implementation(androidx.core.splashscreen)
    implementation(datetime)
  }
}
