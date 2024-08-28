plugins {
    libs.plugins.yadino.run{
        alias(android.library)
        alias(android.hilt)
    }
}

android {
    namespace = "com.rahim.yadino.routine"
}
dependencies {
    api(project(":domain:routine"))
    implementation(project(":core:base"))
}