// Convention plugin: the single executable Spring Boot application (apps/erp-app).
package erp.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.springframework.boot.gradle.plugins.SpringBootPlugin

class SpringBootAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("erp.java-base")
        apply<SpringBootPlugin>()
    }
}
