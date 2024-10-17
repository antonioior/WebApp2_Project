package org.example.crm.integrationTest.controllers

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer

@SpringBootTest
@ContextConfiguration(initializers = [IntegrationTest.Initializer::class])
abstract class IntegrationTest {
    companion object {
        private val db = PostgreSQLContainer("postgres:latest").apply {
            withDatabaseName("crm")
            withUsername("crm")
            withPassword("crm")
            setPortBindings(listOf("12341:5432"))
        }
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            db.start()
            TestPropertyValues.of(
                "spring.datasource.url=${db.jdbcUrl}",
                "spring.datasource.username=${db.username}",
                "spring.datasource.password=${db.password}"
            ).applyTo(
                applicationContext.environment
            )
        }
    }
}