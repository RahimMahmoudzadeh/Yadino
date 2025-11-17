plugins {
  libs.plugins.run {
    alias(library)
    alias (db)
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
