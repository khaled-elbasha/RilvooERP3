// Convention plugin: common Java 25 toolchain + JUnit platform for every subproject.
package example.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

class JavaBaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("java")
        extensions.configure<JavaPluginExtension> {
            toolchain.languageVersion.set(JavaLanguageVersion.of(ConventionVersions.JAVA))
        }
        tasks.withType<JavaCompile>().configureEach {
            options.encoding = "UTF-8"
        }
        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }
    }
}
