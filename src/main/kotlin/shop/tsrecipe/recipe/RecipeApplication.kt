package shop.tsrecipe.recipe

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing
import reactivefeign.spring.config.EnableReactiveFeignClients
import shop.tsrecipe.recipe.properties.ExternalServiceUrl

@EnableConfigurationProperties(value = [ExternalServiceUrl::class])
@EnableMongoAuditing
@EnableReactiveFeignClients
@SpringBootApplication
class RecipeApplication

suspend fun main(args: Array<String>) {
	runApplication<RecipeApplication>(*args)
}
