plugins {
  libs.plugins.run {
    alias(library)
  }
}
kotlin {
  sourceSets{
    commonMain.dependencies{
      projects.run{
        implementation(core.base)
      }
    }
  }
}
