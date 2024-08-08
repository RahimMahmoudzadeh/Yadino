plugins {
    libs.plugins.yadino.run {
        alias(android.library)
    }
}

android {
    namespace = "com.rahim.yadino.domin.routine"
}
dependencies{
    implementation(project(":core:base"))
}
