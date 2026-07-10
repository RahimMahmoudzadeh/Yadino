plugins {
  libs.plugins.run {
    alias(library)
    alias(library.compose)
//    alias(decompose)
    alias(kotlinx.serialization)
  }
}
kotlin {
  sourceSets{
    commonMain.dependencies{
      projects.run{
        implementation(core.base)
      }
      libs.run {
        implementation(accompanist.permissions)
        implementation(swipe)
        api(kotlinx.collections.immutable)
      }
    }
  }
}
