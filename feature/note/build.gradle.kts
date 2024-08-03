plugins {
    libs.plugins.yadino.run {
        alias(android.feature)
        alias(android.library.compose)
    }
}

android {
    namespace = "com.rahim.yadino.feature.note"
}
dependencies{
    implementation(project(":domain:sharedPreferences"))
    implementation(project(":domain:note"))
    implementation(project(":domain:dateTime"))
}
