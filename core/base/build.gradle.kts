plugins {
    libs.plugins.run {
        alias(library)
        alias(di)
        alias(decompose)
        alias(library.compose)
    }
}

android {
    namespace = "com.rahim.yadino.core.base"
}

dependencies {
  projects.run {
    implementation(core.db)
  }
  libs.run {
    implementation(androidx.datastore.preferences)
    implementation(androidx.datastore.preferences.core)
  }
}

