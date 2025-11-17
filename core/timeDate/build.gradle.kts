plugins {
  libs.plugins.run {
    alias(library)
    alias(di)
    alias(db)
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
