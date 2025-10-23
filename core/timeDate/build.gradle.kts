plugins {
  libs.plugins.yadino.run {
    alias(android.library)
    alias(di)
    alias(android.room)
  }
}
android {
  namespace = "com.rahim.yadino.core.timeDate"
}
dependencies{
  projects.run {
    implementation(core.db)
    implementation(core.base)
  }
}
