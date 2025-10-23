plugins {
  libs.plugins.yadino.android.run {
    alias(library)
//    alias(hilt)
    alias(room)
  }
}
android {
  namespace = "com.rahim.yadino.core.db"
}
dependencies{
  libs.run {
//    implementation(gson)
  }
}
