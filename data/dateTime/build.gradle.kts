plugins {
    libs.plugins.yadino.run{
        alias(android.library)
        alias(android.hilt)
    }
}

android {
    namespace = "com.rahim.yadino.dateTime"
}
dependencies {
    api(project(":domain:dateTime"))
    implementation(project(":core:base"))
}