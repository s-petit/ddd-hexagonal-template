plugins {
    `maven-publish`
    id("org.springframework.boot")
}

val staticResources by configurations.creating {
    description = "Used to retrieve static resources needed by the jar"
}

publishing {
    publications {
        create<MavenPublication>("publish") {
            artifactId = rootProject.name
            artifact(tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar"))
        }
    }
}

dependencies {
    // NEMO
    implementation(project(":backend:domain"))
    runtimeOnly(project(":backend:infrastructure"))
    staticResources(project(":frontend", configuration = "angularDist"))

    // REST
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.3")
    runtimeOnly("javax.servlet:javax.servlet-api:4.0.1")
    runtimeOnly("javax.xml.bind:jaxb-api:2.3.1")

    // LOGBACK
    runtimeOnly("ch.qos.logback:logback-access:1.2.3")
    runtimeOnly("net.logstash.logback:logstash-logback-encoder:6.6")
}

tasks {

    val extractStaticResources by registering(Copy::class) {
        dependsOn(staticResources)
        from({ staticResources.map { zipTree(it) } })
        into(file("$buildDir/frontend-angular-dist/BOOT-INF/classes/static"))
    }

    getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
        mainClass.set("com.enjoycode.hexagonal.DddHexagonalApplication")
        archiveBaseName.set(rootProject.name)
        dependsOn(extractStaticResources)
        from(file("$buildDir/frontend-angular-dist"))
    }
}
