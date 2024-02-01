plugins {
    libs.plugins.run{
        alias(androidApplication) apply false
        alias(google.services) apply false
        alias(firebase.crashlytics) apply false
        alias(com.android.library) apply false
        alias(kotlinAndroid) apply false
        alias(hilt.plugin) apply false
        alias(ksp) apply false
        alias(kotlin.parcelize) apply false
    }

//    id 'com.google.devtools.ksp' version '1.9.21-1.0.15' apply false
//    id 'org.jetbrains.kotlin.jvm' version '1.9.21' apply false
}