plugins {
  libs.plugins.run {
    alias(presentation)
    alias(decompose)
    alias(library.compose)
    alias(kotlinx.serialization)
  }
}

android {
  namespace = "com.rahim.yadino.home.presentation"
}
dependencies {
  projects.run {
    implementation(home.domain)
  }

  libs.run {
    implementation(androidx.core.splashscreen)
    implementation(datetime)
    implementation(accompanist.permissions)
  }
}
