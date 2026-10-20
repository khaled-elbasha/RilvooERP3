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
// apps/example-app applies the Boot plugin directly by marker id (resolved from Maven Central,
// where the 4.1.1 plugin artifact is published). This keeps the Kotlin-DSL classpath free of
// the JDK-25 / Kotlin-JVM-target mismatch that the Boot plugin jar triggered previously.

gradlePlugin {
    plugins {
        create("javaBase") {
            id = "example.java-base"
            implementationClass = "example.convention.JavaBaseConventionPlugin"
        }
        create("javaLibrary") {
            id = "example.java-library"
            implementationClass = "example.convention.JavaLibraryConventionPlugin"
        }
        create("bootPlatformCatalog") {
            id = "example.boot-platform-catalog"
            implementationClass = "example.convention.BootPlatformCatalogConventionPlugin"
        }
    }
}
