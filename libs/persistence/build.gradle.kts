// libs:persistence — OPTIONAL persistence infrastructure patterns. Nothing in this module is
// auto-configured against a live DataSource; the foundation starts without any database.
// Concrete applications opt in to JPA/jOOQ/Flyway/Blaze coordinates from the version catalog.
plugins { id("example.java-library") }

dependencies {
    // Compile-time API surface only (no runtime DB requirement):
    compileOnly(libs.jakarta.persistence.api)
    compileOnly(libs.blaze.core.api)
    compileOnly(libs.blaze.entity.view.api)
    testImplementation(libs.spring.boot.starter.test)
}
