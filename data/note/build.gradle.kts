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
    implementation(project(":core:base"))
}