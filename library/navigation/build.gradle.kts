plugins {
  libs.plugins.run {
    alias(library)
    alias(library.compose)
    alias(kotlinx.serialization)
  }
}
kotlin {
  sourceSets{
    commonMain.dependencies{
      projects.run{
        implementation(library.designsystem)
      }
      libs.run{
        implementation(kotlinx.serialization)
      }
    }
  }
}
