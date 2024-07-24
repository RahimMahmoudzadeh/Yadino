plugins {
    libs.plugins.yadino.run{
        alias(android.library)
        alias(android.hilt)
    }
}

android {
    namespace = "com.rahim.yadino.routine_repository"
}
dependencies {
    api(project(":domain:routine"))
    implementation(project(":data:routine-local"))
    implementation(project(":core:base"))
}