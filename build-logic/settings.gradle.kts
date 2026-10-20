// build-logic: minimal Gradle convention plugins shared by all example-project subprojects.
// The Boot BOM version is passed via convention/gradle.properties (example.springBootVersion),
// so no version-catalog wiring into the included build is required.
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
rootProject.name = "build-logic"
include("convention")
