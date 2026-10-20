// erp-systems — Gradle multi-project build (Kotlin DSL).
// Phase 1 capabilities: IAM + Person. Single executable application: apps/erp-app.
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

rootProject.name = "erp-systems"

// ---- Included build with convention plugins ---------------------------------
includeBuild("build-logic")

// ---- Executable application --------------------------------------------------
include("apps:erp-app")

// ---- HTTP interface libraries ------------------------------------------------
include("interfaces:iam-api")
include("interfaces:person-api")

// ---- Business capability modules ---------------------------------------------
include("modules:iam-module")
include("modules:person-module")

// ---- Domain persistence libraries (one per PostgreSQL schema) ----------------
include("domains:iam")
include("domains:person")

// ---- Shared libraries ---------------------------------------------------------
include("libs:common")
include("libs:api-contract")
include("libs:web")
include("libs:security")
include("libs:authorization")
include("libs:access-catalog")
include("libs:audit")
include("libs:persistence")
include("libs:module-runtime")

// ---- Flyway migration resource libraries (per schema, ordered) ---------------
include("database:audit")
include("database:person")
include("database:iam")
