// modules:person-module — Person business capability (CRUD, search, soft delete).
plugins { id("erp.java-library") }

dependencies {
    api(project(":domains:person"))
    api(project(":libs:authorization"))
    implementation(project(":libs:access-catalog"))
    implementation(project(":libs:audit"))
    implementation(project(":libs:persistence"))
    implementation(project(":libs:common"))
    api("org.springframework.data:spring-data-jpa")
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
    implementation("com.blazebit:blaze-persistence-core-api-jakarta")
    implementation("com.blazebit:blaze-persistence-entity-view-api-jakarta")
    annotationProcessor("com.blazebit:blaze-persistence-entity-view-processor-jakarta:1.6.20")

    testImplementation(project(":database:audit"))
    testImplementation(project(":database:person"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-flyway")
    testImplementation("org.flywaydb:flyway-database-postgresql")
    testImplementation("org.postgresql:postgresql")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
}
