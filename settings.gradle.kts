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
    ":feature:home",
    ":feature:routine",
    ":feature:note",
    ":feature:welcome",
    ":feature:wakeup",
    ":feature:calender",
    ":feature:alarmHistory",
    ":data:routine-repository",
    ":data:routine-local",
    ":domain:routine",
    ":domain:sharedPreferences",
    ":data:sharedPreferences",
    ":domain:dateTime",
    ":data:dateTime-local",
    ":data:dateTime-repository",
    ":domain:note",
    ":data:note-repository",
    ":data:note-local",
    ":core:database",
    ":domain:reminder",
    ":data:reminder-repository",
)
