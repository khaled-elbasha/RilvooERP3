// Root build for example-project.
// Library subprojects are plain Java libraries; only apps/example-app applies the Spring Boot plugin.
plugins {
    base
}

allprojects {
    group = "com.rilvoo"
    version = "0.1.0-SNAPSHOT"
}

// Aggregate helper task
tasks.register("verifyAll") {
    group = "verification"
    description = "Build and test every subproject."
    dependsOn(allprojects.filter { it != rootProject }.map { "${it.path}:build" })
}
