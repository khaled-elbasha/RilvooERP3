// libs:persistence — Blaze-Persistence Entity View runtime wiring shared by read-side modules.
plugins { id("erp.java-library") }

dependencies {
    api(platform("com.blazebit:blaze-persistence-bom:1.6.20"))
    api("com.blazebit:blaze-persistence-core-api-jakarta")
    api("com.blazebit:blaze-persistence-entity-view-api-jakarta")
    implementation("com.blazebit:blaze-persistence-core-impl-jakarta")
    implementation("com.blazebit:blaze-persistence-entity-view-impl-jakarta")
    implementation("com.blazebit:blaze-persistence-integration-hibernate-7.0")
    implementation("com.blazebit:blaze-persistence-integration-spring-data-4.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework:spring-context")
}
