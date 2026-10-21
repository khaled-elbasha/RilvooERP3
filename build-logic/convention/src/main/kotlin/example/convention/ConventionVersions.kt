// Single source of truth for the convention plugins. Must stay in lockstep with
// gradle/libs.versions.toml ([versions] springBoot / java) — change both together.
package example.convention

internal object ConventionVersions {
    const val SPRING_BOOT = "4.1.1"
    const val JAVA = 25
}
