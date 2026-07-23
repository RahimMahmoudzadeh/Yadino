plugins {
  libs.plugins.run {
    alias(library.compose)
//    alias(decompose)
    alias(kotlinx.serialization)
  }
}
kotlin {
  sourceSets {
    commonMain.dependencies {
      projects.run {
        implementation(core.base)
      }
      libs.run {
        implementation(swipe)
      }
    }
    androidMain.dependencies {
      libs.run {
//        implementation(accompanist.permissions)

//        api(kotlinx.collections.immutable)
      }
    }
  }
}
