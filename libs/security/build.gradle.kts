// libs:security — JWT resource-server validation, security filter chain, 401/403 JSON envelopes.
// Database-independent: never requires a DataSource. Authorization Server and WebAuthn are
// cataloged options only (see docs/customization.md), NOT activated here.
plugins { id("example.java-library") }

dependencies {
    api(project(":libs:authorization"))
    api(project(":libs:api-contract"))
    api(libs.spring.boot.starter.security)
    api(libs.spring.boot.starter.oauth2.resource.server)
    implementation(project(":libs:web"))
    implementation(libs.spring.boot.starter.validation)
    annotationProcessor(libs.spring.boot.configuration.processor)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)
}
