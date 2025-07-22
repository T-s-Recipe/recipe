package shop.tsrecipe.recipe.service

import shop.tsrecipe.recipe.domain.Recipe
import shop.tsrecipe.recipe.util.buildQueryById
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository

@Repository
class RecipeReader(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) {
    suspend fun findOneById(recipeId: ObjectId): Recipe? {
        return reactiveMongoTemplate.findOne(
            buildQueryById(recipeId),
            Recipe::class.java
        ).awaitSingleOrNull()
    }
}