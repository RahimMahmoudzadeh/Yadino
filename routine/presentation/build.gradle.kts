plugins {
  libs.plugins.yadino.run {
    alias(android.feature)
    alias(android.library.compose)
    alias(decompose)
  }
}

android {
  namespace = "com.rahim.yadino.routine.presentation"
}
dependencies {
  projects.run{
    implementation(routine.domain)
    implementation(core.timeDate)
  }
  libs.run {
    implementation(androidx.core.splashscreen)
    implementation(datetime)
  }
}
