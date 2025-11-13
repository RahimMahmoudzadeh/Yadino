plugins {
  libs.plugins.run {
    yadino.run{
      alias(android.feature)
      alias(android.library.compose)
      alias(decompose)
    }
    alias(kotlinx.serialization)
  }
}

android {
  namespace = "com.rahim.yadino.note.presentation"
}
dependencies {
  projects.run{
    implementation(note.domain)
  }

  libs.run {
    implementation(swipe)
  }
}
