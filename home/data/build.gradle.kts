plugins {
  libs.plugins.run {
    alias(library)
    alias(di)
    alias (db)
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
