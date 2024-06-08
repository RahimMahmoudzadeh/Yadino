import org.jetbrains.kotlin.gradle.dsl.JvmTarget
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
        alias(androidx.room)
        alias(compose.compiler)
    }
}
room {
    schemaDirectory("$projectDir/${libs.versions.schemas.get()}")
}

android {
    val localProperties = Properties()
    localProperties.load(FileInputStream(File(rootProject.rootDir, "local.properties")))
    localProperties.run {
        val storeFileAddress = getProperty(libs.versions.store.file.address.get())
        val storePasswordProperties = getProperty(libs.versions.store.password.get())
        val keyAliasProperties = getProperty(libs.versions.key.alias.get())
        val keyPasswordProperties = getProperty(libs.versions.key.password.get())
        signingConfigs {
            create(libs.versions.release.get()) {
                storeFile = file(storeFileAddress)
                storePassword = storePasswordProperties
                keyAlias = keyAliasProperties
                keyPassword = keyPasswordProperties
            }
        }
    }

    namespace = libs.versions.project.application.id.get()
    compileSdk = libs.versions.project.target.sdk.version.get().toInt()

    defaultConfig {
        applicationId = libs.versions.project.application.id.get()
        minSdk = libs.versions.project.min.sdk.version.get().toInt()
        targetSdk = libs.versions.project.target.sdk.version.get().toInt()
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
            signingConfig = signingConfigs[libs.versions.release.get()]
        }
    }
    flavorDimensions += libs.versions.diimension.get()
    productFlavors {
        create(libs.versions.cafe.bazaar.get()) {
            dimension = libs.versions.diimension.get()
            applicationIdSuffix = libs.versions.yadion.get()
            versionNameSuffix = libs.versions.application.id.cafeBazaar.suffix.get()
        }
        create(libs.versions.myket.get()) {
            dimension = libs.versions.diimension.get()
            applicationIdSuffix = libs.versions.yadion.get()
            versionNameSuffix = libs.versions.application.id.myket.suffix.get()
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
             jvmTarget.set(JvmTarget.JVM_17)
        }
        sourceSets.all {
            languageSettings.enableLanguageFeature("ExplicitBackingFields")
        }
    }
    buildFeatures {
        compose = true
    }
    composeCompiler {
        enableStrongSkippingMode = true
    }
    packaging {
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
        //preview
        implementation(ui.tooling.preview)
        //splash
        implementation(androidx.core.splashscreen)
    }
}