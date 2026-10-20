// libs:web — request-id filter, global exception handling, Jackson configuration for the envelope.
plugins { id("erp.java-library") }

dependencies {
    api(project(":libs:api-contract"))
    api(project(":libs:common"))
    api("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // Correlation of requestId with traceId/spanId via Micrometer Tracing (Brave bridge is chosen app-wide).
    compileOnly("io.micrometer:micrometer-tracing")
}
