// Convention plugin: imports the Spring Boot BOM as a "platform" dependency on the declarable
// configurations of an application project, so version-less coordinates resolve WITHOUT the
// io.spring.dependency-management plugin. Used by apps/example-app.
package example.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class BootPlatformCatalogConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val bootVersion = ConventionVersions.SPRING_BOOT
        val bomNotation: String = "org.springframework.boot:spring-boot-dependencies:$bootVersion"
        // Declarable configurations only (compileClasspath/runtimeClasspath are resolvable-only).
        // implementation covers main compile+runtime; testImplementation covers test compile+runtime.
        dependencies {
            add("implementation", project.dependencies.platform(bomNotation))
            add("testImplementation", project.dependencies.platform(bomNotation))
        }
    }
}
