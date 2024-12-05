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

    alias(spotless)
  }
}
spotless {
  kotlin {
    target("**/*.kt")
    trimTrailingWhitespace()
    indentWithSpaces()
    endWithNewline()
    ktlint("0.48.0")
      .setEditorConfigPath("$projectDir/.editorconfig") // sample unusual placement
      .editorConfigOverride(
        mapOf(
          "indent_size" to 2,
          // intellij_idea is the default style we preset in Spotless, you can override it referring to https://pinterest.github.io/ktlint/latest/rules/code-styles.
          "ktlint_code_style" to "intellij_idea",
        ),
      )
  }
  kotlinGradle {
    target("*.gradle.kts") // default target for kotlinGradle
    ktlint() // or ktfmt() or prettier()
  }
  format("misc") {
    target("**/*.gradle", "**/*.md", "**/.gitignore")
    indentWithSpaces()
    trimTrailingWhitespace()
    endWithNewline()
  }
  format("xml") {
    target("**/*.xml")
    indentWithSpaces()
    trimTrailingWhitespace()
    endWithNewline()
  }
}
