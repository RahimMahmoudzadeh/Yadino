plugins {
  libs.plugins.yadino.run {
    alias(android.library)
    alias(android.hilt)
    alias (android.room)
  }
}

android {
  namespace = "com.rahim.yadino.home.data"
}
dependencies {
  projects.run {
    implementation(core.base)
    implementation(core.db)
    implementation(home.domain)
  }
}
