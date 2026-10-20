// libs:authorization — permission model types and the enforcement port every module uses.
plugins { id("erp.java-library") }

dependencies {
    api(project(":libs:common"))
    implementation("org.springframework.security:spring-security-core")
}
