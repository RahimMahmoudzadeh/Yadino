plugins {
  libs.plugins.run {
    alias(library)
  }
}

android {
  namespace = "com.rahim.yadino.routine.domin"
}
dependencies {
  implementation(projects.core.base)
  implementation(projects.core.timeDate)
}
