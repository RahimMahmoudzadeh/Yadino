plugins {
  libs.plugins.run {
    alias(presentation)
//    alias(library.compose)
//    alias(decompose)
  }
}
kotlin {
  sourceSets{
    commonMain.dependencies{
      projects.run{
        implementation(onboarding.domain)
      }
    }
//    androidMain.dependencies {
//      libs.run {
//        implementation(bundles.accompanist)
//      }
//    }
  }
}
