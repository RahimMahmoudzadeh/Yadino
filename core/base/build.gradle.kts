import org.gradle.kotlin.dsl.invoke

plugins {
    libs.plugins.run {
//        alias(di)
//        alias(decompose)
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

