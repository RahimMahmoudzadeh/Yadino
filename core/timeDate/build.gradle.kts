plugins {
  libs.plugins.run {
    alias(library)
//    alias(di)
//    alias(db)
  }
}

kotlin {
  sourceSets{
    commonMain.dependencies{
      projects.run{
        implementation(core.db)
        implementation(core.base)
      }
    }
  }
}
