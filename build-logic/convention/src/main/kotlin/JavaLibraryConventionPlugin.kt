// Convention plugin: java-library project that consumes the Spring Boot BOM via a platform dependency.
// Library projects are plain JARs; they never apply the executable Spring Boot plugin.
package erp.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.dependencies

class JavaLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("erp.java-base")
        pluginManager.apply("java-library")
        // Version catalog is accessible in every project that applies this convention.
        val libs = extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java)
            .named("libs")
        val bootVersion = libs.findVersion("springBoot").get().requiredVersion
        dependencies {
            add("implementation", platform("org.springframework.boot:spring-boot-dependencies:$bootVersion"))
        }
    }
}
