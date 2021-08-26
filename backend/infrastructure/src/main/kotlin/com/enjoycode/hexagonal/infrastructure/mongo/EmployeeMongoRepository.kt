package com.enjoycode.hexagonal.infrastructure.mongo

import com.enjoycode.hexagonal.domain.Employee
import com.enjoycode.hexagonal.domain.EmployeeRepository
import com.mongodb.client.MongoCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.save
import org.springframework.stereotype.Component

@Component
class EmployeeMongoRepository(private val collection: MongoCollection<EmployeeMongo>) :
    EmployeeRepository {

    override fun findById(id: String): Employee? =
            collection.findOne(Employee::id eq id)?.toEmployee()

    override fun save(employee: Employee) =
            collection.save(EmployeeMongo(employee.id, employee.name))

}
