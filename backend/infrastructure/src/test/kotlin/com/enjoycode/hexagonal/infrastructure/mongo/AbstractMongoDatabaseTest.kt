package com.enjoycode.hexagonal.infrastructure.mongo

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@ExtendWith(SpringExtension::class)
@ContextConfiguration(initializers = [AbstractMongoDatabaseTest.Companion.TestDatasourceContextInitializer::class])
@Import(value = [MongoConfig::class])
@TestPropertySource("classpath:application-integration.properties")
abstract class AbstractMongoDatabaseTest {

    companion object {
        private val mongoServer: GenericContainer<Nothing> = GenericContainer<Nothing>("mongo:" + "3.2.17")
            .apply {
                withExposedPorts(27017)
                waitingFor(HostPortWaitStrategy())
            }

        init {
            mongoServer.start()
        }

        // Rewrite Database URL depending on container IP and Port before Spring initialization.
        internal class TestDatasourceContextInitializer :
            ApplicationContextInitializer<ConfigurableApplicationContext> {

            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
                TestPropertyValues.of(
                    "spring.data.mongodb.uri=${mongoDbUri()}"
                ).applyTo(configurableApplicationContext.environment)
            }
        }

        fun mongoDbUri() = "mongodb://${mongoServer.containerIpAddress}:${mongoServer.firstMappedPort}"
    }
}
