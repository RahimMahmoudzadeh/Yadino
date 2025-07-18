plugins {
  libs.plugins.yadino.android.run {
    alias(library)
    alias(hilt)
    alias(room)
  }
}
android {
  namespace = "com.rahim.yadino.db"
}
dependencies{
  libs.run {
//    implementation(gson)
  }
}
