package com.enjoycode.hexagonal.domain

interface MyCollectionRepository {
    fun findById(id: String): MyCollection?
    fun save(myCollection: MyCollection)
}