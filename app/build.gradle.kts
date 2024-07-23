import convention.YadinoBuildType

plugins {
    libs.plugins.yadino.run {
        alias(android.application)
        alias(android.application.compose)
        alias(android.hilt)
        alias(android.application.firebase)
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
    libs.apply {
        implementation(androidx.core.splashscreen)
//        implementation(androidx.tracing.ktx)
        implementation(timber)

        implementation(project(":feature:home"))

        implementation(kotlinx.collections.immutable)
    }
}