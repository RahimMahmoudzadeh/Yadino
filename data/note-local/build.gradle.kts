plugins {
    libs.plugins.yadino.run{
        alias(android.library)
        alias(android.hilt)
        alias(android.room)
    }
}

android {
    namespace = "com.rahim.yadino.note_local"
}