import convention.YadinoBuildType

plugins {
  libs.plugins.run {
    alias(application)
    alias(application.compose)
    alias(firebase)
    alias(db)
    alias(decompose)
    alias(di)
    alias(kotlinx.serialization)
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
