// libs:security — JWT resource-server validation + optional OAuth2 Authorization Server wiring.
plugins { id("erp.java-library") }

dependencies {
    api(project(":libs:authorization"))
    api(project(":domains:iam"))
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    // Authorization Server integration is deliberately opt-in at runtime (see docs/security.md).
    implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}
