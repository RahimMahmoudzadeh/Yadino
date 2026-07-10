plugins {
  libs.plugins.run {
    alias(library)
  }
}
kotlin {
  sourceSets{
    commonMain.dependencies{
      projects.run{
        implementation(projects.core.base)
        implementation(projects.core.timeDate)      }
    }
  }
}
