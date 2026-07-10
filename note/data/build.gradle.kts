plugins {
  libs.plugins.run {
    alias(library)
//    alias (db)
//    alias (di)
  }
}
kotlin {
  sourceSets{
    commonMain.dependencies{
      projects.run{
        implementation(core.base)
        implementation(core.db)
        implementation(note.domain)      }
    }
  }
}
