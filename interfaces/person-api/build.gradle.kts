// interfaces:person-api — versioned REST JSON controllers for the Person capability (library, not executable).
plugins { id("erp.java-library") }

dependencies {
    api(project(":modules:person-module"))
    api(project(":libs:api-contract"))
    implementation(project(":libs:web"))
    implementation(project(":libs:module-runtime"))
    implementation(project(":libs:authorization"))
    implementation(project(":libs:access-catalog"))
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.1.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}
