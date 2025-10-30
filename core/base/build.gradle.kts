plugins {
    libs.plugins.yadino.run {
        alias(android.library)
        alias(di)
        alias(decompose)
        alias(android.library.compose)
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

