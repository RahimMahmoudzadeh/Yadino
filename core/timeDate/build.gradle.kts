plugins {
  libs.plugins.yadino.android.run {
    alias(library)
//    alias(hilt)
    alias(room)
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
