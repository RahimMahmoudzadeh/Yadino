plugins {
    libs.plugins.yadino.run {
        alias(android.library)
        alias(android.hilt)
    }
}

android {
    namespace = "com.rahim.yadino.data.sharedPreferences"
}
dependencies {
    api(project(":domain:sharedPreferences"))
    implementation(project(":core:base"))
}