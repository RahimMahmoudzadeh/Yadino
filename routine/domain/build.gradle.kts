plugins {
  libs.plugins.yadino.run {
    alias(android.library)
  }
}

android {
  namespace = "com.rahim.yadino.routine.domin"
}
dependencies {
  implementation(projects.core.base)
}
