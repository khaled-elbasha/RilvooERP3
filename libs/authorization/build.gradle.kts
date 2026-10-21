// libs:authorization — database-independent authorization abstractions (fail-closed by default).
plugins { id("example.java-library") }

dependencies {
    api(project(":libs:common"))
    api(libs.spring.security.core)
    implementation(libs.spring.context)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)
}
