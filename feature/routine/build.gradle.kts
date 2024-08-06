plugins {
    libs.plugins.yadino.run {
        alias(android.feature)
        alias(android.library.compose)
    }
}

android {
    namespace = "com.rahim.yadino.feature.routine"
}
dependencies{
    implementation(project(":domain:reminder"))
    implementation(project(":domain:dateTime"))
    implementation(project(":domain:sharedPreferences"))
    libs.run {
        implementation(bundles.accompanist)
    }
}
