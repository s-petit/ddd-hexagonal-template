plugins {
    kotlin("jvm")
    id("io.spring.dependency-management")
    id("org.jetbrains.kotlin.plugin.spring")

    id("org.springframework.boot")
    // Apply the application plugin to add support for building a CLI application.
    application
}

repositories {
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    // REST
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.3")
    runtimeOnly("javax.servlet:javax.servlet-api:4.0.1")
    runtimeOnly("javax.xml.bind:jaxb-api:2.3.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.5.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.3.31")
    testImplementation("org.assertj:assertj-core:3.12.2")


}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    // Define the main class for the application.
    mainClassName = "ddd-hexagonal.DddHexagonalApplication"
}


tasks {
    withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }

    withType(Test::class).configureEach {
        useJUnitPlatform()
    }
}