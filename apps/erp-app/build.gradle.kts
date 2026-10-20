// apps:erp-app — the ONLY executable Spring Boot application in erp-systems.
// Composition/bootstrap only; no business controller/service logic lives here.
plugins {
    // The Boot Gradle plugin marker is resolved from Maven Central (verified 4.1.1 exists there).
    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.7"
    // Java 25 toolchain + UTF-8 + JUnit platform conventions shared with library projects.
    id("erp.java-base")
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

    testImplementation(platform("org.testcontainers:testcontainers-bom:1.21.4"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.modulith:spring-modulith-test")
    testImplementation(platform("org.springframework.modulith:spring-modulith-bom:2.1.1"))
    testImplementation("com.tngtech.archunit:archunit-junit5:1.4.0")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.1.1")
    // Logbook is verified on the test+runtime classpath of the app (Boot 4 compatibility proven here).
    implementation("org.zalando:logbook-spring-boot-starter:4.2.0")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:2.1.1")
    }
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveBaseName.set("erp-systems-app")
}
