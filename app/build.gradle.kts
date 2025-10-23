import convention.YadinoBuildType

plugins {
  libs.plugins.yadino.run {
    alias(android.application)
    alias(android.application.compose)
    alias(android.application.firebase)
    alias(android.room)
    alias(di)
  }
}
android {
  buildFeatures {
    buildConfig = true
  }
  buildTypes {
    debug {
      applicationIdSuffix = YadinoBuildType.DEBUG.applicationIdSuffix
    }
    val release by getting {
      isMinifyEnabled = true
      isShrinkResources = true
      applicationIdSuffix = YadinoBuildType.RELEASE.applicationIdSuffix
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro",
      )
      signingConfig = signingConfigs.getByName("debug")
    }
  }
}
dependencies {
  libs.run {
    implementation(androidx.core.splashscreen)
    implementation(androidx.constraintlayout)
    implementation(timber)
    implementation(kotlinx.collections.immutable)
    implementation(accompanist.permissions)
  }
}
