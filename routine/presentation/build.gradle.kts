plugins {
  libs.plugins.yadino.run {
    alias(android.feature)
    alias(android.library.compose)
  }
}

android {
  namespace = "com.rahim.yadino.routine.presentation"
}
dependencies {
  projects.run{
    implementation(home.domain)
  }
}
