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
                name = command.name,
                imageUrl = command.imageUrl,
                servings = command.servings,
                cost = command.cost,
                cookingTime = command.cookingTime,
                ingredients = command.ingredients.map { it.toEntity() },
                sections = command.sections.map { it.toEntity() },
                description = command.description,
            )
        ).awaitSingle()
    }
}