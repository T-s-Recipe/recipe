package shop.tsrecipe.recipe.service

import shop.tsrecipe.recipe.domain.Recipe
import shop.tsrecipe.recipe.repository.RecipeRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Repository

@Repository
class RecipeManager(
    private val recipeRepository: RecipeRepository
) {
    suspend fun create(command: CreateRecipeCommand): Recipe {
        return recipeRepository.save(
            Recipe(
                authorId = command.authorId,
                authorNickname = command.authorNickname!!,
                title = command.title,
                imageUrl = command.imageUrl!!,
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
}