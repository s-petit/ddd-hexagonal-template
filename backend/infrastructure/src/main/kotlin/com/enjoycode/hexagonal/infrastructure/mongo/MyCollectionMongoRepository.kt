package com.enjoycode.hexagonal.infrastructure.mongo

import com.enjoycode.hexagonal.domain.MyCollection
import com.enjoycode.hexagonal.domain.MyCollectionRepository
import com.mongodb.client.MongoCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.save
import org.springframework.stereotype.Component

@Component
class MyCollectionMongoRepository(private val collection: MongoCollection<MyCollectionMongo>) :
    MyCollectionRepository {

    override fun findById(id: String): MyCollection? =
            collection.findOne(MyCollection::id eq id)?.toMyCollection()

    override fun save(myCollection: MyCollection) =
            collection.save(MyCollectionMongo(myCollection.id, myCollection.name))

}
