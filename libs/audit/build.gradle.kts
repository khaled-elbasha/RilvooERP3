// libs:audit — reusable audit contracts only (events, context, publisher port, sinks).
// Deliberately has NO JPA entity, NO repository and NO schema: durability is provided by a
// future application-supplied AuditSink (PostgreSQL, Kafka, ...). Never claims durability alone.
plugins { id("example.java-library") }

dependencies {
    api(project(":libs:common"))
    api(libs.slf4j.api)
    implementation(libs.spring.context)
    testImplementation(libs.spring.boot.starter.test)
}
