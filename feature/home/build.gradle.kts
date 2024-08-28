plugins {
    libs.plugins.yadino.run {
        alias(android.feature)
        alias(android.library.compose)
    }
}

android {
    namespace = "com.rahim.yadino.feature.home"
}
dependencies {
    implementation(project(":data:reminder"))
    implementation(project(":data:dateTime"))
    implementation(project(":data:sharedPreferences"))
}