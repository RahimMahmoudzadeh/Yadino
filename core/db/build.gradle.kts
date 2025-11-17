plugins {
  libs.plugins.run {
    alias(library)
    alias(di)
    alias(db)
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
