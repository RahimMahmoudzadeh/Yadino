plugins {
    libs.plugins.run{
        alias(androidApplication) apply false
        alias(com.android.library) apply false
        alias(kotlinAndroid) apply false
        alias(hilt.plugin) apply false
        alias(ksp) apply false
        alias(kotlin.parcelize) apply false
        alias(androidx.room) apply false
        alias(google.services) apply false
        alias(firebase.crashlytics) apply false
    }
}