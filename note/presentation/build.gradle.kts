plugins {
  libs.plugins.run {
    alias(presentation)
//    alias(library.compose)
//    alias(decompose)
    alias(kotlinx.serialization)
  }
}
kotlin {
  sourceSets{
    commonMain.dependencies{
      projects.run{
        implementation(note.domain)
      }
//      libs.run{
//        implementation(swipe)
//      }
    }
  }
}
