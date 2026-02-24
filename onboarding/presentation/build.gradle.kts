plugins {
  libs.plugins.run {
    alias(presentation)
    alias(library.compose)
    alias(decompose)
  }
}

android {
  namespace = "com.rahim.yadino.onboarding.presentation"
}
dependencies {
  projects.run{
    implementation(onboarding.domain)
  }
  libs.run {
    implementation(bundles.accompanist)
  }
}
