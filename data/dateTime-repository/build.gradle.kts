plugins {
    libs.plugins.yadino.run{
        alias(android.library)
        alias(android.hilt)
    }
}

android {
    namespace = "com.rahim.yadino.dateTime_repository"
}
dependencies {
    api(project(":domain:dateTime"))
    implementation(project(":data:dateTime-local"))
    implementation(project(":core:base"))
}