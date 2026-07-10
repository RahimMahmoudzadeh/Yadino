plugins {
  libs.plugins.run {
    alias(presentation)
//    alias(decompose)
    alias(library.compose)
    alias(kotlinx.serialization)
  }
}
kotlin {
  sourceSets{
    commonMain.dependencies{
      projects.run{
        implementation(home.domain)
      }
    }
    androidMain.dependencies{
      libs.run {
        implementation(androidx.core.splashscreen)
        implementation(datetime)
        implementation(accompanist.permissions)
      }
    }
  }
}
