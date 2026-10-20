// Convention plugin: makes the Gradle version catalog available inside apps (Spring Boot applications)
// and imports the Spring Boot BOM as an "platform" dependency on all main/test configurations, so that
// version-less catalog aliases resolve without the io.spring.dependency-management plugin.
package erp.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

class BootPlatformCatalogConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
        val bootVersion = libs.findVersion("springBoot").get().requiredVersion
        // Declarable configurations only (compileClasspath/runtimeClasspath are resolvable-only).
        // implementation covers main compile+runtime; testImplementation covers test compile+runtime.
        // 'api' is wired lazily because it exists only when java-library is applied (not for plain apps).
        val bomProvider = dependencyFactory.platform(
            "org.springframework.boot:spring-boot-dependencies:$bootVersion",
        )
        listOf("implementation", "testImplementation").forEach { name ->
            dependencies.add(name, bomProvider.get())
        }
        configurations.matching { it.name == "api" }.all {
            dependencies.add("api", bomProvider.get())
        }
    }
}
