plugins {
    libs.plugins.yadino.run{
        alias(android.library)
        alias(android.hilt)
    }
}

android {
    namespace = "com.rahim.yadino.note"
}
dependencies {
    api(project(":domain:note"))
    api(project(":domain:sharedPreferences"))
    implementation(project(":core:base"))
}
