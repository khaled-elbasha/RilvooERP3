// example-project — reusable Spring Boot enterprise foundation (Gradle Kotlin DSL multi-project).
// Database-independent by design: no production module requires a DataSource to start.
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
}

rootProject.name = "example-project"

// ---- Included build with convention plugins ---------------------------------
includeBuild("build-logic")

// ---- Executable application (the ONLY project applying the Spring Boot plugin)
include("apps:example-app")

// ---- Shared libraries --------------------------------------------------------
include("libs:common")
include("libs:api-contract")
include("libs:web")
include("libs:security")
include("libs:authorization")
include("libs:audit")
include("libs:persistence")
include("libs:module-runtime")
include("libs:observability")
