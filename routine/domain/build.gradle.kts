plugins {
  libs.plugins.yadino.run {
    alias(android.library)
//    alias(android.hilt)
  }
}

android {
  namespace = "com.rahim.yadino.routine.domin"
}
dependencies {
  implementation(projects.core.base)
  implementation(projects.core.timeDate)
}
