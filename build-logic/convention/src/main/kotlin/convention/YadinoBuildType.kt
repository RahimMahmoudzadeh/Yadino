
package convention

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class YadinoBuildType(val applicationIdSuffix: String? = null) {
  DEBUG(".debug"),
  RELEASE,
}
