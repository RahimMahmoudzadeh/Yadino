plugins {
    libs.plugins.run {
    alias(androidApplication) apply false
    alias(com.android.library) apply false
    alias(kotlinAndroid) apply false
    alias(hilt.plugin) apply false
    alias(ksp) apply false
    alias(kotlin.parcelize) apply false
    alias(androidx.room) apply false
    alias(google.services) apply false
    alias(firebase.crashlytics) apply false
    alias(compose.compiler) apply false
    alias(jetbrains.kotlin.jvm) apply false
    alias(spotless)
  }
}
spotless {
  kotlin {
    target("**/*.kt")
    trimTrailingWhitespace()
    leadingTabsToSpaces()
    endWithNewline()
    ktlint("0.48.0")
      .setEditorConfigPath("$projectDir/.editorconfig")
      .editorConfigOverride(
        mapOf(
          "indent_size" to 2,
          "ktlint_code_style" to "intellij_idea",
        ),
      )
  }
  kotlinGradle {
    target("*.gradle.kts")
    ktlint()
  }
  format("misc") {
    target("**/*.gradle", "**/*.md", "**/.gitignore")
    leadingTabsToSpaces()
    trimTrailingWhitespace()
    endWithNewline()
  }
  format("xml") {
    target("**/*.xml")
    leadingTabsToSpaces()
    trimTrailingWhitespace()
    endWithNewline()
  }
}
