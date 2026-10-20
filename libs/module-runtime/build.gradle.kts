// libs:module-runtime — validates erp.runtime.apis.enabled and gates REST interfaces per capability.
plugins { id("erp.java-library") }

dependencies {
    api(project(":libs:common"))
    api("org.springframework.boot:spring-boot-starter-webmvc")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}
