package com.enjoycode.hexagonal.domain

interface EmployeeRepository {
    fun findById(id: String): Employee?
    fun save(employee: Employee)
}