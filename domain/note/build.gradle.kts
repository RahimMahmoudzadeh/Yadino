plugins {
    libs.plugins.yadino.run {
        alias(android.library)
        alias(android.room)
    }
}

android {
    namespace = "com.rahim.yadino.domin.note"
}

dependencies{
    implementation(project(":core:base"))
}
