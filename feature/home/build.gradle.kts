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
    implementation(project(":data:reminder-repository"))
    implementation(project(":data:dateTime-repository"))
    implementation(project(":data:sharedPreferences"))
}