plugins {
  libs.plugins.yadino.run {
    alias(android.library)
    alias(di)
    alias(android.room)
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
