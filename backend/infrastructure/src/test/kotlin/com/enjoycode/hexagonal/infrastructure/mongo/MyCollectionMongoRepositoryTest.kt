package com.enjoycode.hexagonal.infrastructure.mongo

import com.enjoycode.hexagonal.domain.MyCollection
import com.mongodb.client.MongoCollection
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(value = [MyCollectionMongoRepositoryTest.Config::class])
internal class MyCollectionMongoRepositoryTest(
    @Autowired private val myCollectionMongoRepository: MyCollectionMongoRepository,
    @Autowired private val collection: MongoCollection<MyCollectionMongo>
) : AbstractMongoDatabaseTest() {

    @BeforeEach
    internal fun dropCollection() {
        collection.drop()
    }

    @Test
    fun `should return null when not present in database`() {
        val drvById = myCollectionMongoRepository.findById("unknown-id")
        assertThat(drvById).isNull()
    }

    @Test
    fun `should return registration information when present in database`() {
        val myCollection = MyCollection("id", "name")
        myCollectionMongoRepository.save(myCollection
        )

        val result = myCollectionMongoRepository.findById("id")

        assertThat(result).isEqualTo(myCollection)
    }


    @Configuration
    internal class Config {
        @Bean
        fun drvRegistrationMongoRepository(collection: MongoCollection<MyCollectionMongo>):
                MyCollectionMongoRepository = MyCollectionMongoRepository(collection)
    }
}
