// apps:erp-app — the ONLY executable Spring Boot application in erp-systems.
// Composition/bootstrap only; no business controller/service logic lives here.
plugins {
    // The Boot Gradle plugin marker is resolved from Maven Central (verified 4.1.1 exists there).
    id("org.springframework.boot") version "4.1.1"
    // Java 25 toolchain + UTF-8 + JUnit platform conventions shared with library projects.
    id("erp.java-base")
    // Applies the Spring Boot BOM (spring-boot-dependencies:4.1.1) as an imported platform for every
    // configuration, including testRuntime/testCompile and annotationProcessor paths. Replaces the old
    // io.spring.dependency-management plugin so that catalog aliases WITHOUT versions resolve correctly.
    id("erp.boot-platform-catalog")
}

dependencies {
    implementation(project(":interfaces:iam-api"))
    implementation(project(":interfaces:person-api"))
    implementation(project(":libs:security"))
    implementation(project(":libs:web"))
    implementation(project(":libs:module-runtime"))
    implementation(project(":libs:audit"))
    implementation(project(":libs:persistence"))
    implementation(project(":libs:authorization"))
    implementation(project(":libs:access-catalog"))
    // Migrations are NOT run by replicas (spring.flyway.enabled=false); they ship so a dedicated
    // migration task/profile can locate them on the classpath (see docs/migrations.md).
    runtimeOnly(project(":database:audit"))
    runtimeOnly(project(":database:person"))
    runtimeOnly(project(":database:iam"))

    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    // Chosen tracing bridge: Brave (single coherent Micrometer bridge; no OTel installed alongside).
    implementation("org.springframework.boot:spring-boot-micrometer-tracing-brave")
    // HTTP client for outbound integrations when needed (no WebFlux/R2DBC anywhere).
    implementation("org.springframework.boot:spring-boot-starter-restclient")
    runtimeOnly("org.postgresql:postgresql")

    // --- Cataloged platform capabilities, deliberately NOT on the Phase-1 runtime classpath ---
    // See docs/architecture.md "Activation matrix". They become `implementation` only when a real
    // use case + infrastructure verification exists:
    //   spring-boot-starter-freemarker, spring-boot-starter-mail, spring-boot-starter-data-redis,
    //   spring-boot-starter-cache, spring-boot-starter-jooq, jobrunr-spring-boot-4-starter,
    //   spring-boot-starter-oauth2-authorization-server (enabled via profile in tests, see below).

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    // Spring Modulith BOM must precede version-less Modulith coordinates in the same configuration.
    testImplementation(platform(libs.spring.modulith.bom))
    testImplementation(libs.spring.modulith.test)
    // Boot 4 Testcontainers integration (@ServiceConnection / @Testcontainers containers as beans).
    // Test-only; never on the production runtime classpath.
    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(libs.archunit.junit5)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.junit)
    testImplementation(libs.springdoc.starter.webmvc.ui)
    // Logbook is verified on the test+runtime classpath of the app (Boot 4 compatibility proven here).
    implementation("org.zalando:logbook-spring-boot-starter:4.2.0")
}


tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveBaseName.set("erp-systems-app")
}
