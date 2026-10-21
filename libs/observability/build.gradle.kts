// libs:observability — actuator exposure policy, correlation snapshot helper.
plugins { id("example.java-library") }

dependencies {
    api(project(":libs:web"))            // CorrelationKeys is part of the exposed helper API
    api(libs.micrometer.registry.prometheus)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.context)
    compileOnly(libs.micrometer.tracing)
    annotationProcessor(libs.spring.boot.configuration.processor)
    testImplementation(libs.spring.boot.starter.test)
}
