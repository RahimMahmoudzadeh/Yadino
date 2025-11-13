plugins {
  libs.plugins.run {
    yadino.run {
      alias(android.feature)
      alias(android.library.compose)
      alias(decompose)
    }
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
  }
}
