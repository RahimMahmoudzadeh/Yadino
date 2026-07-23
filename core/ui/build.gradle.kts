plugins {
  libs.plugins.run {
    alias(library.compose)
  }
}


kotlin {
  sourceSets{
    commonMain.dependencies{
      projects.run{
        implementation(core.db)
      }
      libs.run {
        implementation(androidx.datastore.preferences)
        implementation(androidx.datastore.preferences.core)
      }
    }
  }
}
