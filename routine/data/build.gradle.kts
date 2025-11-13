plugins {
  libs.plugins.yadino.run {
    alias(android.library)
    alias(di)
    alias (android.room)
  }
}

android {
  namespace = "com.rahim.yadino.routine.data"
}
dependencies {
  projects.run {
    implementation(core.base)
    implementation(core.db)
    implementation(core.timeDate)
    implementation(routine.domain)
  }
}
