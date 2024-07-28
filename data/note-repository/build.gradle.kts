plugins {
    libs.plugins.yadino.run{
        alias(android.library)
        alias(android.hilt)
    }
}

android {
    namespace = "com.rahim.yadino.note_repository"
}
dependencies {
    api(project(":domain:note"))
    implementation(project(":data:note-local"))
    implementation(project(":core:base"))
}