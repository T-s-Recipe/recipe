package shop.tsrecipe.recipe.service

import kotlinx.coroutines.reactor.awaitSingle
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import shop.tsrecipe.recipe.domain.Recipe
import shop.tsrecipe.recipe.repository.RecipeRepository
import shop.tsrecipe.recipe.util.buildQueryById

@Repository
class RecipeManager(
    private val recipeRepository: RecipeRepository,
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) {
    suspend fun create(command: CreateRecipeCommand): Recipe {
        return recipeRepository.save(
            Recipe(
                authorId = command.authorId,
                authorNickname = command.authorNickname!!,
                title = command.title,
                imageUrl = command.imageUrl,
                servings = command.servings,
                cost = command.cost,
                cookingTime = command.cookingTime,
                memo = command.memo,
                basicIngredients = command.basicIngredients.map { it.toEntity() },
                sourceIngredients = command.sourceIngredients.map { it.toEntity() },
                steps = command.steps.map { it.toEntity() }
            )
        ).awaitSingle()
    }

    suspend fun update(command: UpdateRecipeCommand): Recipe {
        val update = Update()

        update.apply {
            command.title?.let { this.set("title", it) }
            command.imageUrl?.let { this.set("imageUrl", it) }
            command.servings?.let { this.set("servings", it) }
            command.cost?.let { this.set("cost", it) }
            command.cookingTime?.let { this.set("cookingTime", it) }
            command.memo?.let { this.set("memo", it) }
            command.basicIngredients?.let { this.set("basicIngredients", it.map { ingredient -> ingredient.toEntity() }) }
            command.sourceIngredients?.let { this.set("sourceIngredients", it.map { ingredient -> ingredient.toEntity() }) }
            command.steps?.let { this.set("steps", it.map { step -> step.toEntity() }) }
        }

        return reactiveMongoTemplate.findAndModify(
            buildQueryById(command.recipeId),
            update,
            Recipe::class.java
        ).awaitSingle()
    }

    suspend fun delete(recipeId: ObjectId) {
        recipeRepository.deleteById(recipeId)
    }
}