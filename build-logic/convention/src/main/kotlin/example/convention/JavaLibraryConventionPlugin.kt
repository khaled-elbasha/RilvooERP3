// Convention plugin: java-library project that consumes the Spring Boot BOM via a platform
// dependency. Library projects are plain JARs; they never apply the executable Spring Boot plugin.
// The Boot version is read from build-logic/convention/gradle.properties so this plugin does not
// depend on the main build's version catalog being wired into included builds.
package example.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class JavaLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("example.java-base")
        pluginManager.apply("java-library")
        val bootVersion = ConventionVersions.SPRING_BOOT
        dependencies {
            add("implementation", project.dependencies.platform("org.springframework.boot:spring-boot-dependencies:$bootVersion"))
            // The Boot BOM governs compile/runtime/test configurations only. Annotation-processor
            // paths (e.g. spring-boot-configuration-processor) are NOT BOM-managed, so pin the
            // version here for any project declaring one without a version.
            add("annotationProcessor", "org.springframework.boot:spring-boot-configuration-processor:$bootVersion")
        }
    }
}
