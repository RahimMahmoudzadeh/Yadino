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
//        implementation(androidx.tracing.ktx)
    implementation(timber)
    implementation(kotlinx.collections.immutable)
    implementation(accompanist.permissions)

    implementation(project(":feature:welcome"))
    implementation(project(":feature:note"))
    implementation(project(":feature:routine"))
    implementation(project(":feature:calender"))

    implementation(project(":domain:sharedPreferences"))
    implementation(project(":domain:dateTime"))
    implementation(project(":domain:routine"))
    implementation(project(":domain:note"))
  }
}
