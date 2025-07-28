package shop.tsrecipe.recipe.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.bson.types.ObjectId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import shop.tsrecipe.recipe.domain.toResponse
import shop.tsrecipe.recipe.domain.toSliceResponse
import shop.tsrecipe.recipe.service.RecipeService
import shop.tsrecipe.recipe.util.baseResponse

@Tag(name = "Recipe", description = "Recipe APIs")
@RequestMapping
@RestController
class RecipeController(
    private val recipeService: RecipeService
) {
    @Operation(
        summary = "레시피 등록",
        description = "레시피 등록 API"
    )
    @PostMapping
    suspend fun generate(@RequestBody request: CreateRecipeRequest): ResponseEntity<RecipeResponse> {
        return baseResponse(
            body = recipeService.create(request.toCommand()).toResponse()
        )
    }

    @Operation(
        summary = "레시피 단건 조회",
        description = "레시피 단건 조회 by recipeId"
    )
    @GetMapping("/{recipeId}")
    suspend fun getRecipe(@PathVariable recipeId: String): ResponseEntity<RecipeResponse> {
        return baseResponse(
            body = recipeService.getRecipe(ObjectId(recipeId)).toResponse()
        )
    }

    @Operation(
        summary = "최신 레시피 목록 조회",
        description = "등록된 날짜 기준으로 내림차순 레시피 목록 조회 (메인화면 활용)"
    )
    @GetMapping("/recent")
    suspend fun getRecentRecipes(
        @RequestParam(required = false) limit: Int = 15,
        @RequestParam(required = false) cursorId: String? = null
    ): ResponseEntity<RecipeSliceResponse> {
        return baseResponse(
            body = recipeService.getRecentList(limit, cursorId).toSliceResponse()
        )
    }

//    @Operation(
//        summary = "레시피 목록 조회",
//        description = """
//            # 레시피 목록 조회
//            각 파라미터 별 조건 확인 필수
//        """
//    )
//    @GetMapping
//    suspend fun getRecipes(
//        @Parameter(description = "레시피 등록 회원 ID")
//        @RequestParam authorId: String?,
//
//        @Parameter(description = "검색 키워드 (레시피 이름)")
//        @RequestParam keyword: String?,
//
//        @Valid condition: RecipeSearchCondition
//    ) {
//
//    }
}