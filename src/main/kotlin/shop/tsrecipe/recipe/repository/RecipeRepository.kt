package shop.tsrecipe.recipe.repository

import shop.tsrecipe.recipe.domain.Recipe
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface RecipeRepository: ReactiveMongoRepository<Recipe, ObjectId> {
}