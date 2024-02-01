import java.io.FileInputStream
import java.util.Properties

plugins {
    libs.plugins.run {
        alias(androidApplication)
        alias(hilt.plugin)
        alias(kotlinAndroid)
        alias(ksp)
        alias(kotlin.parcelize)
        alias(google.services)
        alias(firebase.crashlytics)
    }
}

android {
    val localProperties = Properties()
    localProperties.load(FileInputStream(File(rootProject.rootDir, "local.properties")))
    localProperties.run {
        val storeFileAddress = getProperty("storeFileAddress")
        val storePasswordProperties = getProperty("storePassword")
        val keyAliasProperties = getProperty("keyAlias")
        val keyPasswordProperties = getProperty("keyPassword")
        signingConfigs {
            create("release") {
                storeFile = file(storeFileAddress)
                storePassword = storePasswordProperties
                keyAlias = keyAliasProperties
                keyPassword = keyPasswordProperties
            }
        }
    }

    namespace = "com.rahim"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rahim"
        minSdk = 26
        targetSdk = 34
        versionCode = 111
        versionName = "1.1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        ksp {
            arg(RoomSchemaArgProvider(File(projectDir, "schemas")))
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
//            signingConfig = signingConfigs.release
        }
    }
    flavorDimensions += "rahim"
    productFlavors {
        create("cafeBazaar") {
            dimension = "rahim"
            applicationIdSuffix = ".yadino"
            versionNameSuffix = "-c"
        }
        create("myket") {
            dimension = "rahim"
            applicationIdSuffix = ".yadino"
            versionNameSuffix = "-m"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    libs.run {
        //junit
        testImplementation(junit)
        //test
        androidTestImplementation(bundles.test)
        //core
        implementation(androidx.core)
        //compose
        implementation(platform(androidx.compose.bom))
        androidTestImplementation(platform(androidx.compose.bom))
        debugImplementation(ui.tooling)
        implementation(bundles.compose)
        //room
        implementation(bundles.room)
        annotationProcessor(androidx.room.compiler)
        ksp(androidx.room.compiler)
        //hilt
        implementation(bundles.hilt)
        ksp(hilt.compiler)
        //navigation
        implementation(bundles.navigation)
        //timber
        implementation(timber)
        //lifecycle
        implementation(bundles.lifecycle)
        //firebase
        implementation(platform(firebaseBom))
        implementation(bundles.firebase)
        //samanzamani
        implementation(samanzamani)
        //dataTime
        implementation(datetime)
        //swipe
        implementation(swipe)
        //pager
        implementation(bundles.accompanist)
    }
}