package config

import org.gradle.api.JavaVersion

object Config {
    val android = AndroidConfig(
        minSdkVersion = 26,
        targetSdkVersion = 34,
        compileSdkVersion = 34,
        applicationId = "com.rahim.yadino",
        versionCode = 125,
        versionName = "1.4.0",
        nameSpace = "com.rahim.yadino",
        versionNameSuffixCafeBazaar ="-c",
        versionNameSuffixGooglePlay ="-g",
        versionNameSuffixMyket ="-m",
        applicationIdSuffix = ".yadino",
        dimension = "contentType"
    )
    val jvm = JvmConfig(
        javaVersion = JavaVersion.VERSION_17,
        kotlinJvm = JavaVersion.VERSION_17.toString(),
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    )
}
data class AndroidConfig(
    val minSdkVersion : Int,
    val targetSdkVersion : Int,
    val compileSdkVersion : Int,
    val applicationId : String,
    val versionCode : Int,
    val versionName : String,
    val nameSpace: String,
    val versionNameSuffixCafeBazaar: String,
    val versionNameSuffixMyket: String,
    val versionNameSuffixGooglePlay: String,
    val applicationIdSuffix: String,
    val dimension: String,
)
data class JvmConfig(
    val javaVersion : JavaVersion,
    val kotlinJvm : String,
    val freeCompilerArgs : List<String>
)