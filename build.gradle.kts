// Root build for erp-systems.
// Library subprojects are plain Java libraries; only apps/erp-app applies the Spring Boot plugin.
plugins {
    base
}

allprojects {
    group = "com.rilvoo.erp"
    version = "0.1.0-SNAPSHOT"
}

// Aggregate helper tasks
tasks.register("verifyAll") {
    group = "verification"
    description = "Build and test every subproject."
    dependsOn(allprojects.map { if (it != rootProject) "${it.path}:build" else null }.filterNotNull())
}
