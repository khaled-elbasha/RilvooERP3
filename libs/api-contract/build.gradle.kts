// libs:api-contract — transport-neutral response envelope records shared by all REST interfaces.
plugins { id("example.java-library") }

dependencies {
    api(libs.jackson.annotations) // version from the Boot BOM platform import
    testImplementation(libs.spring.boot.starter.test)
}
