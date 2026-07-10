plugins {
  libs.plugins.run {
    alias(cmp)
    alias(di)
//    alias(navigation)
  }
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      projects.run {
        implementation(home.data)
        implementation(routine.data)
        implementation(note.data)
      }
    }
  }
}
