plugins {
  libs.plugins.yadino.run {
    alias(android.library)
    alias (android.room)
    alias (di)
  }
}

android {
  namespace = "com.rahim.yadino.note.data"
}
dependencies {
  projects.run {
    implementation(core.base)
    implementation(core.db)
    implementation(note.domain)
  }
}
