package com.enjoycode.hexagonal.infrastructure.mongo

import com.enjoycode.hexagonal.domain.Employee
import com.mongodb.client.MongoCollection
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(value = [EmployeeMongoRepositoryTest.Config::class])
internal class EmployeeMongoRepositoryTest(
    @Autowired private val employeeMongoRepository: EmployeeMongoRepository,
    @Autowired private val collection: MongoCollection<EmployeeMongo>
) : AbstractMongoDatabaseTest() {

    @BeforeEach
    internal fun dropCollection() {
        collection.drop()
    }

    @Test
    fun `should return null when not present in database`() {
        val unknownEmployee = employeeMongoRepository.findById("unknown-id")
        assertThat(unknownEmployee).isNull()
    }

    @Test
    fun `should return registration information when present in database`() {
        val employee = Employee("id", "name")
        employeeMongoRepository.save(employee)

        val result = employeeMongoRepository.findById("id")

        assertThat(result).isEqualTo(employee)
    }

    @Configuration
    internal class Config {
        @Bean
        fun employeeMongoRepository(collection: MongoCollection<EmployeeMongo>): EmployeeMongoRepository =
            EmployeeMongoRepository(collection)
    }
}
