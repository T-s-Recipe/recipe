package shop.tsrecipe.recipe.service

import org.bson.types.ObjectId
import shop.tsrecipe.recipe.domain.Recipe
import shop.tsrecipe.recipe.exception.BaseException
import shop.tsrecipe.recipe.exception.ErrorCode
import org.springframework.stereotype.Service
import shop.tsrecipe.recipe.external.member.MemberService

@Service
class RecipeService(
    private val recipeManager: RecipeManager,
    private val recipeReader: RecipeReader,
    private val memberService: MemberService
) {
    suspend fun create(command: CreateRecipeCommand): Recipe {
        setAuthorInfoIsMember(command)
        return recipeManager.create(command)
    }

    private suspend fun setAuthorInfoIsMember(command: CreateRecipeCommand) {
        val member = memberService.getMemberById(command.authorId.toString())
            ?: throw BaseException(ErrorCode.MEMBER_NOT_FOUND)
        command.setNickname(member.nickname)
    }

    suspend fun getRecipe(recipeId: ObjectId): Recipe {
        return recipeReader.findOneById(recipeId) ?: throw BaseException(ErrorCode.RECIPE_NOT_FOUND)
    }

    suspend fun getRecentList(limit: Int, cursorId: String?): List<Recipe> {
        return recipeReader.findAllByCursor(
            GetRecentCommand(limit = limit, cursorId = cursorId?.let { ObjectId(it) })
        )
    }
}