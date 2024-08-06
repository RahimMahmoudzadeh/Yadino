plugins {
    libs.plugins.yadino.run {
        alias(android.library)
        alias(android.hilt)
    }
}

android {
    namespace = "com.rahim.yadino.domin.reminder"
}
dependencies{
    implementation(project(":core:base"))
    api(project(":domain:routine"))
}
