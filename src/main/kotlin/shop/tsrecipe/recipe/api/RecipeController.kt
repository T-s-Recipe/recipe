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
import shop.tsrecipe.recipe.external.S3Service
import shop.tsrecipe.recipe.service.RecipeService
import shop.tsrecipe.recipe.service.SearchRecipeCommand
import shop.tsrecipe.recipe.util.baseResponse

@Tag(name = "Recipe", description = "Recipe APIs")
@RequestMapping
@RestController
class RecipeController(
    private val recipeService: RecipeService,
    private val s3Service: S3Service
) {
    @Operation(
        summary = "레시피 등록",
        description = "레시피 등록 API"
    )
    @PostMapping
    suspend fun generate(
        @RequestBody request: CreateRecipeRequest
    ): ResponseEntity<RecipeResponse> {
        return baseResponse(
            body = recipeService.create(command = request.toCommand()).toResponse()
        )
    }

    @Operation(
        summary = "파일 업로드 URL 요청",
        description = "S3에 업로드할 수 있는 URL을 반환"
    )
    @PostMapping("/file-upload")
    suspend fun upload(@RequestBody request: FileUploadRequest): ResponseEntity<FileUploadResponse> {
        return baseResponse(
            body = s3Service.getPresignedUrl(request.fileName, ContentType.of(request.contentType))
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

    @Operation(
        summary = "레시피 조건 검색",
        description = """
            # 레시피 조건 검색
            - 각 파라미터 별 조건 확인 필수
        """
    )
    @GetMapping
    suspend fun getRecipes(
        @Parameter(description = "레시피 작성자 Member ID")
        @RequestParam(required = false) authorId: String? = null,

        @Parameter(description = "등록한 Member 닉네임")
        @RequestParam(required = false) nickname: String? = null,

        @Parameter(description = "레시피 검색 키워드")
        @RequestParam(required = false) searchKeyword: String? = null,

        @Parameter(description = "레시피 검색 필터")
        @Valid filter: RecipeSearchFilter? = null,

        @RequestParam(required = false) limit: Int = 15,

        @RequestParam(required = false) cursorId: String? = null
    ): ResponseEntity<RecipeSliceResponse> {
        return baseResponse(
            body = recipeService.getRecipes(
                SearchRecipeCommand(
                    authorId = authorId?.let { ObjectId(it) },
                    nickname = nickname,
                    searchKeyword = searchKeyword,
                    costGte = filter?.costGte,
                    costLte = filter?.costLte,
                    cookingTimeGte = filter?.cookingTimeGte,
                    cookingTimeLte = filter?.cookingTimeLte,
                    limit = limit,
                    cursorId = cursorId?.let { ObjectId(it) }
                )
            ).toSliceResponse()
        )
    }
}