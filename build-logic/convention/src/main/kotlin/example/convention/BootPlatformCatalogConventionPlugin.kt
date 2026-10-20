// Convention plugin: imports the Spring Boot BOM as a "platform" dependency on the declarable
// configurations of an application project, so version-less coordinates resolve WITHOUT the
// io.spring.dependency-management plugin. Used by apps/example-app.
package example.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class BootPlatformCatalogConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val bootVersion = providers.gradleProperty("example.springBootVersion")
            .getOrElse(error("Set example.springBootVersion in build-logic/convention/gradle.properties"))
        // Declarable configurations only (compileClasspath/runtimeClasspath are resolvable-only).
        // implementation covers main compile+runtime; testImplementation covers test compile+runtime.
        val bomProvider = dependencyFactory.platform(
            "org.springframework.boot:spring-boot-dependencies:$bootVersion",
        )
        listOf("implementation", "testImplementation").forEach { name ->
            dependencies.add(name, bomProvider.get())
        }
    }
}
