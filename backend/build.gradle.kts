import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
    id("io.spring.dependency-management")
    id("org.jetbrains.kotlin.plugin.spring")
    jacoco
    // Apply the application plugin to add support for building a CLI application.
    application
}

val coverageExclusions = listOf(
    "/**/com/vidal/nemo/application/**/*Config.*",
    "/**/com/vidal/nemo/MainApplication.*"
)

jacoco {
    toolVersion = "0.8.7"
    reportsDirectory.set(file("$buildDir/customJacocoReportDir"))
}

val kotlinVersion = "${project.property("kotlin.version")}"

subprojects {
    tasks {
        apply(plugin = "org.jetbrains.kotlin.jvm")
        apply(plugin = "jacoco")
        apply(plugin = "org.jlleitschuh.gradle.ktlint")

        withType(KotlinCompile::class) {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_11.toString()
                kotlinOptions.allWarningsAsErrors = true
            }
        }

        withType(Test::class).configureEach {
            maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
            useJUnitPlatform()
            systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
        }

        jacocoTestReport {
            reports {
                xml.required.set(false)
                csv.required.set(false)
                html.outputLocation.set(file("$buildDir/jacocoHtml"))
            }
        }

        withType(JacocoReport::class.java).named("jacocoTestReport") {
            classDirectories.setFrom(
                files(
                    classDirectories.files.map {
                        fileTree(it).apply {
                            exclude(coverageExclusions)
                        }
                    }
                )
            )
        }
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation("io.github.microutils:kotlin-logging:2.0.6")
        implementation("org.slf4j:slf4j-api:1.7.30") { because("Required by kotlin-logging") }

        // Unit Tests
        testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlinVersion")
        testImplementation("org.assertj:assertj-core:3.19.0")
        testImplementation("io.mockk:mockk:1.11.0")
    }
}

val springModule: (String) -> (Boolean) = { module -> module == "application" || module == "infrastructure" }
configure(subprojects.filter { springModule(it.name) }) {
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")

    dependencies {
        runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
        implementation(platform("org.springframework.boot:spring-boot-dependencies:2.5.3"))
        implementation("org.springframework.boot:spring-boot-starter-test") {
            exclude("org.junit.vintage:junit-vintage-engine")
            exclude("org.assertj:assertj-core")
            exclude("org.mockito:mockito-core")
            exclude("org.mockito:mockito-junit-jupiter")
        }
    }
}
