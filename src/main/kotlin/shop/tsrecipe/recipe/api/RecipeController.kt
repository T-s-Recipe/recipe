package shop.tsrecipe.recipe.api

import shop.tsrecipe.recipe.domain.toResponse
import shop.tsrecipe.recipe.service.RecipeService
import shop.tsrecipe.recipe.util.baseResponse
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import org.bson.types.ObjectId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping
@RestController
class RecipeController(
    private val recipeService: RecipeService
) {
    @PostMapping
    suspend fun create(@RequestBody request: CreateRecipeRequest): ResponseEntity<RecipeResponse> {
        return baseResponse(
            body = recipeService.create(request.toCommand()).toResponse()
        )
    }

    @GetMapping("/{recipeId}")
    suspend fun getRecipe(@PathVariable recipeId: String): ResponseEntity<RecipeResponse> {
        return baseResponse(
            body = recipeService.getRecipe(ObjectId(recipeId)).toResponse()
        )
    }

    /** TODO 레시피 목록 조회 (무한스크롤 15개씩)
     * 메인 화면에서 호출되는 조건 필드에 대해 index 걸어야함
     */
    @GetMapping
    suspend fun getRecipes(
        @Parameter(description = "레시피 등록 회원 ID")
        @RequestParam authorId: String?,

        @Parameter(description = "검색 키워드 (레시피 이름)")
        @RequestParam keyword: String?,

        @Valid condition: RecipeSearchCondition
    ) {

    }
}