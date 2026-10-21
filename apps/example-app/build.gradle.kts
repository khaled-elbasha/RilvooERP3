// apps:example-app — the ONLY executable Spring Boot application in example-project.
// Composition/bootstrap only; reusable infrastructure lives in libs/*. No database is required.
plugins {
    // Boot plugin marker resolved from Maven Central (4.1.1 published there; verified during ERP build).
    id("org.springframework.boot") version "4.1.1"
    // Java 25 toolchain + UTF-8 + JUnit platform conventions shared with library projects.
    id("example.java-base")
    // Imports the Spring Boot BOM as a platform on implementation/testImplementation so that
    // version-less catalog aliases resolve without io.spring.dependency-management.
    id("example.boot-platform-catalog")
}

dependencies {
    implementation(project(":libs:web"))
    implementation(project(":libs:security"))
    implementation(project(":libs:authorization"))
    implementation(project(":libs:audit"))
    implementation(project(":libs:module-runtime"))
    implementation(project(":libs:observability"))
    implementation(project(":libs:api-contract"))
    implementation(project(":libs:common"))

    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.oauth2.resource.server)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.micrometer.registry.prometheus)
    // Chosen tracing bridge: Brave (single coherent Micrometer bridge; no OTel installed alongside).
    // Brings io.micrometer:micrometer-tracing-bridge-brave transitively — do NOT declare both.
    implementation(libs.spring.boot.micrometer.tracing.brave)
    // HTTP client for outbound integrations when needed (no WebFlux/R2DBC anywhere).
    implementation(libs.spring.boot.starter.restclient)
    // OpenAPI/Swagger for registered controllers only.
    implementation(libs.springdoc.starter.webmvc.ui)
    // Zalando Logbook (HTTP request/response logging with masking) — active, configured in libs:web.
    implementation(libs.logbook.spring.boot.starter)

    annotationProcessor(libs.spring.boot.configuration.processor.get().let { "${it.group}:${it.name}:${libs.versions.springBoot.get()}" })

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)
    testImplementation(libs.archunit.junit5)
    // Spring Modulith BOM must precede version-less Modulith coordinates in the same configuration.
    testImplementation(platform(libs.spring.modulith.bom))
    testImplementation(libs.spring.modulith.test)
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveBaseName.set("example-app")
}
