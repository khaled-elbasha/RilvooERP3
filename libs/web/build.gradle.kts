// libs:web — request-id filter, global exception handling, Logbook masking config, OpenAPI setup.
plugins { id("example.java-library") }

dependencies {
    api(project(":libs:api-contract"))
    api(project(":libs:common"))
    api(libs.spring.boot.starter.webmvc)   // brings spring-boot-autoconfigure + context + web MVC
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.jackson.databind)  // ObjectMapper used by exception/error serialization paths
    // Zalando Logbook: HTTP request/response logging with strict sensitive-data masking.
    api(libs.logbook.spring.boot.starter)
    // SpringDoc for the OpenAPI foundation (customizer beans + grouping). UI starter comes from apps.
    implementation(libs.springdoc.starter.webmvc.ui)
    // Correlation of requestId with traceId/spanId via Micrometer Tracing (Brave bridge chosen app-wide).
    compileOnly(libs.micrometer.tracing)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)
    testRuntimeOnly(libs.micrometer.tracing)
}
