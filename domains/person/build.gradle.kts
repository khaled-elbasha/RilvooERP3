// domains:person — authoritative JPA mappings for the PostgreSQL `person` schema. JPA APIs only.
plugins { id("erp.java-library") }

dependencies {
    api(project(":libs:common"))
    api("jakarta.persistence:jakarta.persistence-api")
}
