// libs:module-runtime — validates app.runtime.apis.enabled and gates REST interfaces per module.
plugins { id("example.java-library") }

dependencies {
    api(project(":libs:common"))
    api(libs.spring.boot.starter.webmvc)
    annotationProcessor(libs.spring.boot.configuration.processor)
    testImplementation(libs.spring.boot.starter.test)
}
