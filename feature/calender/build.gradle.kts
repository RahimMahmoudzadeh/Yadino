plugins {
    libs.plugins.yadino.run {
        alias(android.feature)
        alias(android.library.compose)
    }
}

android {
    namespace = "com.rahim.yadino.feature.calender"
}
dependencies{
    implementation(project(":data:dateTime"))
    libs.run {
        implementation(bundles.vico)
    }
    implementation(project(":data:sharedPreferences"))
}