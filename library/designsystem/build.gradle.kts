plugins {
    libs.plugins.yadino.run {
        alias(android.library)
        alias(android.library.compose)
    }
}

android {
    namespace = "com.rahim.yadino.library.designsystem"
}
dependencies{
    implementation(project(":core:base"))
    libs.run {
        implementation(accompanist.permissions)
        implementation(swipe)
        implementation(datetime)
    }
}