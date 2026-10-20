plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Compile-time access to the Spring Boot Gradle plugin so a convention plugin can apply it.
    compileOnly("org.springframework.boot:spring-boot-gradle-plugin:4.1.1")
}

gradlePlugin {
    plugins {
        create("javaBase") {
            id = "erp.java-base"
            implementationClass = "erp.convention.JavaBaseConventionPlugin"
        }
        create("javaLibrary") {
            id = "erp.java-library"
            implementationClass = "erp.convention.JavaLibraryConventionPlugin"
        }
        create("springBootApp") {
            id = "erp.spring-boot-app"
            implementationClass = "erp.convention.SpringBootAppConventionPlugin"
        }
    }
}
