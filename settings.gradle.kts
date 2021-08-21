rootProject.name = "ddd-hexagonal-template"
include("frontend")
include("backend")

//include("backend:application")
//include("backend:domain")
//include("backend:infrastructure")

pluginManagement {
    val kotlinVersion = "${extra.properties["kotlin.version"]}"
    plugins {
        id("io.spring.dependency-management") version "1.0.11.RELEASE"
        id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
        id("org.springframework.boot") version "2.5.3"
        kotlin("jvm") version kotlinVersion
    }
}
