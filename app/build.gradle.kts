import convention.YadinoBuildType

plugins {
    libs.plugins.yadino.run {
        alias(android.application)
        alias(android.application.compose)
        alias(android.hilt)
        alias(android.application.firebase)
        alias(android.room)
    }
}
android {
    namespace = "com.rahim.yadino"
    buildTypes {
        debug {
            applicationIdSuffix = YadinoBuildType.DEBUG.applicationIdSuffix
        }
        val release by getting {
            isMinifyEnabled = true
            applicationIdSuffix = YadinoBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}
dependencies {
    libs.run {
        implementation(androidx.core.splashscreen)
//        implementation(androidx.tracing.ktx)
        implementation(timber)

        implementation(project(":feature:home"))
        implementation(project(":feature:welcome"))
        implementation(project(":feature:note"))
        implementation(project(":feature:routine"))
        implementation(project(":feature:alarmHistory"))
        implementation(project(":feature:wakeup"))
        implementation(project(":feature:calender"))

        implementation(project(":core:database"))
        implementation(project(":domain:sharedPreferences"))
        implementation(project(":domain:routine"))
        implementation(project(":domain:dateTime"))
        implementation(project(":domain:note"))

        implementation(kotlinx.collections.immutable)
        implementation(bundles.room)
    }
}