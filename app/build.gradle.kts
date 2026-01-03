import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import convention.YadinoBuildType
import java.io.FileInputStream
import java.util.Properties

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
  val keystorePropertiesFile = rootProject.file("local.properties")
  val keystoreProperties = Properties()
  if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
  }

  signingConfigs {
    create("release") {
      storeFile = rootProject.file("yadinoKst.jks")
      storePassword = keystoreProperties.getProperty("storePassword")
      keyAlias = keystoreProperties.getProperty("keyAlias")
      keyPassword = keystoreProperties.getProperty("keyPassword")
    }
  }
  applicationVariants.all {
    outputs.forEach { output ->
      if (output is ApkVariantOutputImpl) {
        output.outputFileName = "yadino.apk"
      }
    }
  }
  buildFeatures {
    buildConfig = true
  }
  buildTypes {
    debug {
      applicationIdSuffix = YadinoBuildType.DEBUG.applicationIdSuffix
    }
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      applicationIdSuffix = YadinoBuildType.RELEASE.applicationIdSuffix
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro",
      )
      signingConfig = signingConfigs.getByName("release")
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
