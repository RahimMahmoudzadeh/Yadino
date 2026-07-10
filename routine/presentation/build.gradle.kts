plugins {
  libs.plugins.run {
    alias(presentation)
    alias(library.compose)
//    alias(decompose)
    alias(kotlinx.serialization)
  }
}
kotlin {
  sourceSets{
    commonMain.dependencies{
      projects.run{
        implementation(routine.domain)
        implementation(core.timeDate)
      }
    }
    androidMain.dependencies {
      libs.run {
        implementation(androidx.core.splashscreen)
        implementation(datetime)
        implementation(accompanist.permissions)
      }
    }
  }
}

