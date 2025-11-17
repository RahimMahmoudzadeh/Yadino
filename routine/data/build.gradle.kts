plugins {
  libs.plugins.run {
    alias(library)
    alias(di)
    alias (db)
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
