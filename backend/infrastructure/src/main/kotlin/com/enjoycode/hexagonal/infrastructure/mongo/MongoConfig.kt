package com.enjoycode.hexagonal.infrastructure.mongo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration

@Configuration
class MongoConfig(
    @Value("\${spring.data.mongodb.uri}") private val mongoClientUri: String,
    @Value("\${spring.data.mongodb.database}") private val dbName: String
) :
    AbstractMongoClientConfiguration() {

    override fun getDatabaseName(): String {
        return dbName
    }

    override fun mongoClient(): MongoClient {
        return KMongo.createClient(mongoClientUri)
    }

    @Bean
    fun kmongo(): MongoDatabase = mongoClient().getDatabase(dbName)

    @Bean
    fun Employee(kmongo: MongoDatabase): MongoCollection<EmployeeMongo> =
        kmongo.getCollection<EmployeeMongo>("employee")

}
