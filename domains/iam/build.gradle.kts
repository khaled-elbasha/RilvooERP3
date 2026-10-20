// domains:iam — authoritative JPA mappings for the PostgreSQL `iam` schema. JPA APIs only.
plugins { id("erp.java-library") }

dependencies {
    api(project(":libs:common"))
    api("jakarta.persistence:jakarta.persistence-api")
}
