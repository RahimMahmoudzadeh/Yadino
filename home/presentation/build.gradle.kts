plugins {
  libs.plugins.yadino.run {
    alias(android.feature)
    alias(decompose)
    alias(android.library.compose)
  }
}

android {
  namespace = "com.rahim.yadino.home.presentation"
}
dependencies {
  projects.run{
    implementation(home.domain)
  }

  libs.run {
    implementation(androidx.core.splashscreen)
    implementation(datetime)
  }
}
