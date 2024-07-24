plugins {
    libs.plugins.yadino.run {
        alias(android.library)
        alias(android.hilt)
        alias(android.library.compose)
    }
}

android {
    namespace = "com.rahim.yadino.core.base"
}

