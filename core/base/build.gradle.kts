plugins {
    libs.plugins.yadino.run {
        alias(android.library)
        alias(android.hilt)
        alias(android.library.compose)
        alias(android.room)
    }
}

android {
    namespace = "com.rahim.yadino.core.base"
}

