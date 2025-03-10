plugins {
    libs.plugins.yadino.run {
        alias(android.library)
        alias(android.hilt)
    }
}

android {
    namespace = "com.rahim.yadino.domin.routine"
}
dependencies{
  implementation(projects.core.base)
}
