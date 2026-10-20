// libs:observability — actuator/Prometheus/tracing-safe wiring helpers and log correlation keys.
plugins { id("example.java-library") }

dependencies {
    api(project(":libs:web"))
    api(libs.micrometer.registry.prometheus)
    implementation(libs.spring.boot.starter.actuator)
    compileOnly(libs.micrometer.tracing)
    testImplementation(libs.spring.boot.starter.test)
}
