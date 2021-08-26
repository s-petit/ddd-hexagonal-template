package com.enjoycode.hexagonal.infrastructure.mongo

import com.enjoycode.hexagonal.domain.Employee

data class EmployeeMongo(
    val id: String,
    val name: String
) {
    fun toEmployee() = Employee(id, name)
}
