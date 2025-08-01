package shop.tsrecipe.recipe.service

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import org.springframework.http.codec.multipart.FilePart
import shop.tsrecipe.recipe.domain.Recipe
import shop.tsrecipe.recipe.exception.BaseException
import shop.tsrecipe.recipe.exception.ErrorCode
import org.springframework.stereotype.Service
import shop.tsrecipe.recipe.external.S3Service
import shop.tsrecipe.recipe.external.member.MemberService
import java.lang.Exception

@Service
class RecipeService(
    private val recipeManager: RecipeManager,
    private val recipeReader: RecipeReader,
    private val memberService: MemberService,
    private val s3Service: S3Service
) {
    suspend fun create(
        imageFile: FilePart,
        command: CreateRecipeCommand
    ): Recipe {
        return try {
            coroutineScope {
                launch { setAuthorInfoIsMember(command) }
                val thumbnailDeferred = async { s3Service.upload(imageFile) }
                val thumbnailUrl = thumbnailDeferred.await()

                command.setImage(thumbnailUrl)

                recipeManager.create(command)
            }
        } catch (e: Exception) {
            throw BaseException(ErrorCode.RECIPE_CREATE_FAILED)
        }
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