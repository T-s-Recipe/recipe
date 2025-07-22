package shop.tsrecipe.recipe.service

import shop.tsrecipe.recipe.domain.Recipe
import shop.tsrecipe.recipe.exception.BaseException
import shop.tsrecipe.recipe.exception.ErrorCode
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class RecipeService(
    private val recipeManager: RecipeManager,
    private val recipeReader: RecipeReader
) {
    suspend fun create(command: CreateRecipeCommand): Recipe {
        // TODO authorId 확인하는 로직 추가해야됨
        return recipeManager.create(command)
    }

    suspend fun getRecipe(recipeId: ObjectId): Recipe {
        return recipeReader.findOneById(recipeId) ?: throw BaseException(ErrorCode.RECIPE_NOT_FOUND)
    }
}