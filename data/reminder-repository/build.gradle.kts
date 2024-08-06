plugins {
    libs.plugins.yadino.run{
        alias(android.library)
        alias(android.hilt)
    }
}

android {
    namespace = "com.rahim.yadino.reminder_repository"
}
dependencies {
    implementation(project(":domain:reminder"))
    implementation(project(":core:base"))
}