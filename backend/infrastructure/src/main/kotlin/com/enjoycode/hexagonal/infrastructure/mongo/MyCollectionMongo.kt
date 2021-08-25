package com.enjoycode.hexagonal.infrastructure.mongo

import com.enjoycode.hexagonal.domain.MyCollection

data class MyCollectionMongo(
    val id: String,
    val name: String
) {

    fun toMyCollection() = MyCollection(id, name)
}
