plugins {
  libs.plugins.run {
    alias(presentation)
    alias(library.compose)
    alias(decompose)
    alias(kotlinx.serialization)
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
    implementation(accompanist.permissions)
  }
}
