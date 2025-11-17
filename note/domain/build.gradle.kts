plugins {
  libs.plugins.run {
    alias(library)
  }
}

android {
  namespace = "com.rahim.yadino.note.domin"
}
dependencies {
  implementation(projects.core.base)
}
