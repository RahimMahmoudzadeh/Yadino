gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
rootProject.name = "Yadino"
include(
    ":app",
    ":core:base",
    ":library:designsystem",
    ":library:navigation",
    ":feature:routine",
    ":feature:note",
    ":feature:welcome",
    ":feature:calender",
    ":data:routine",
    ":domain:routine",
    ":domain:sharedPreferences",
    ":data:sharedPreferences",
    ":domain:dateTime",
    ":data:dateTime",
    ":domain:note",
    ":data:note",
)
include(":data:lib")
include(":data:database")
