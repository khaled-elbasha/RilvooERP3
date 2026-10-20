// libs:audit — actor/request context holder + append-only audit writer port and JPA implementation.
plugins { id("erp.java-library") }

dependencies {
    api(project(":libs:common"))
    api("jakarta.persistence:jakarta.persistence-api")
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.data:spring-data-jpa")
}
