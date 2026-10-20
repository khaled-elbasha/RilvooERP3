plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

// Java 25 toolchain for build-logic itself. Declared inline (not via a self-referencing
// convention plugin) because a project cannot apply its own gradlePlugin in the plugins block.
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks.withType<JavaCompile>().configureEach { options.encoding = "UTF-8" }

// NOTE: no compileOnly dependency on org.springframework.boot:spring-boot-gradle-plugin.
// apps/erp-app applies the Boot plugin directly by marker id (resolved from Maven Central,
// where the 4.1.1 plugin artifact is published). This keeps the Kotlin-DSL classpath free of
// the JDK-25 / Kotlin-JVM-target mismatch that the Boot plugin jar triggered here.

gradlePlugin {
    plugins {
        create("javaBase") {
            id = "erp.java-base"
            implementationClass = "erp.convention.JavaBaseConventionPlugin"
        }
        create("javaLibrary") {
            id = "erp.java-library"
            implementationClass = "erp.convention.JavaLibraryConventionPlugin"
        }
        create("bootPlatformCatalog") {
            id = "erp.boot-platform-catalog"
            implementationClass = "erp.convention.BootPlatformCatalogConventionPlugin"
        }
    }
}
