plugins {
    libs.plugins.yadino.run {
        alias(android.library)
    }
}

android {
    namespace = "com.rahim.yadino.domin.note"
}

dependencies{
  implementation(projects.core.base)
}
