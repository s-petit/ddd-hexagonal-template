dependencies {

// domain
    implementation(project(":backend:domain"))

// Mongo
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    val kmongoVersion = "4.2.7"
    implementation("org.litote.kmongo:kmongo:$kmongoVersion")
    implementation("org.litote.kmongo:kmongo-id:$kmongoVersion")

// ITs
    testImplementation(platform("org.testcontainers:testcontainers-bom:1.15.3"))
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
}
