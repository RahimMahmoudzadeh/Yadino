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
    implementation(project(":data:sharedPreferences"))
    implementation(project(":data:note-repository"))
    implementation(project(":data:dateTime-repository"))
}
