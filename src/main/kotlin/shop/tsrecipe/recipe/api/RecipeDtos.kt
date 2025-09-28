package shop.tsrecipe.recipe.api

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Positive
import org.bson.types.ObjectId
import shop.tsrecipe.recipe.domain.IngredientUnit
import shop.tsrecipe.recipe.exception.BaseException
import shop.tsrecipe.recipe.exception.ErrorCode
import shop.tsrecipe.recipe.service.*
import java.util.*

@Schema(description = "레시피 등록 RequestDTO")
data class CreateRecipeRequest(
    @field:Schema(description = "레시피 제목")
    val title: String,

    @field:Schema(description = "이미지 URL")
    val imageUrl: String,

    @field:Schema(description = "용량 (defaultValue: 4)")
    val servings: Int = 4,

    @field:Schema(description = "비용")
    val cost: Int? = null,

    @field:Schema(description = "요리 소요 시간 (분)")
    val cookingTime: Int? = null,

    @field:Schema(description = "메모")
    val memo: String? = null,

    @field:Schema(description = "기본 재료")
    val basicIngredients: List<IngredientRequest>,

    @field:Schema(description = "소스 재료")
    val sourceIngredients: List<IngredientRequest>,

    @field:Schema(description = "요리 단계 목록")
    val steps: List<StepRequest>,
) {
    fun toCommand(memberId: ObjectId): CreateRecipeCommand {
        return CreateRecipeCommand(
            authorId = memberId,
            title = this.title,
            imageUrl = this.imageUrl,
            servings = this.servings,
            cost = this.cost,
            cookingTime = this.cookingTime,
            memo = this.memo,
            basicIngredients = this.basicIngredients.map { it.toCommandInfo() },
            sourceIngredients = this.sourceIngredients.map { it.toCommandInfo() },
            steps = this.steps.map { it.toCommandInfo() }
        )
    }
}

@Schema(description = "기본 재료 RequestDTO")
data class IngredientRequest(
    @field:Schema(description = "재료 이름")
    val name: String,

    @field:Schema(description = "재료 용량 상세")
    val measurements: List<MeasurementRequest>
) {
    fun toCommandInfo(): IngredientInfo {
        return IngredientInfo(
            name = this.name.trim().lowercase(Locale.ENGLISH),
            measurements = this.measurements.map { MeasurementInfo(it.amount, it.unit) }
        )
    }
}

data class MeasurementRequest(
    @field:Schema(description = "양")
    val amount: Int,

    @field:Schema(description = "단위 (QUANTITY / GRAM / / MILLILITER / TABLESPOON / TEASPOON / CUP / OZ")
    val unit: IngredientUnit
)

@Schema(description = "레시피 Step RequestDTO")
data class StepRequest(
    @field:Schema(description = "제목")
    val title: String,

    @field:Schema(description = "상세 과정 목록")
    val processes: List<ProcessRequest>,
) {
    fun toCommandInfo(): StepInfo {
        return StepInfo(
            title = this.title,
            processes = this.processes.map { it.toCommandInfo() }
        )
    }
}

@Schema(description = "레시피 Step 상세 과정")
data class ProcessRequest(
    @field:Schema(description = "과정 내용")
    val content: String
) {
    fun toCommandInfo(): ProcessInfo {
        return ProcessInfo(
            content = this.content
        )
    }
}

@Schema(description = "레시피 ResponseDTO")
data class RecipeResponse(
    @field:Schema(description = "레시피 ID")
    val id: String,

    @field:Schema(description = "레시피 등록 회원 ID")
    val authorId: String,

    @field:Schema(description = "레시피 등록 회원 닉네임")
    val authorNickname: String,

    @field:Schema(description = "레시피 제목")
    val title: String,

    @field:Schema(description = "이미지 URL")
    val imageUrl: String,

    @field:Schema(description = "용량")
    val servings: Int,

    @field:Schema(description = "비용")
    val cost: Int?,

    @field:Schema(description = "소요 시간 (분)")
    val cookingTime: Int?,

    @field:Schema(description = "메모")
    val memo: String?,

    @field:Schema(description = "기본 재료 목록")
    val basicIngredients: List<IngredientResponse>,

    @field:Schema(description = "소스 재료 목록")
    val sourceIngredients: List<IngredientResponse>,

    @field:Schema(description = "레시피 Step 목록")
    val steps: List<StepResponse>
)

@Schema(description = "기본 재료 ResponseDTO")
data class IngredientResponse(
    @field:Schema(description = "이름")
    val name: String,

    @field:Schema(description = "재료 양 상세 목록")
    val measurements: List<MeasurementResponse>
)

@Schema(description = "재료 양 상세 ResponseDTO")
data class MeasurementResponse(
    @field:Schema(description = "양")
    val amount: Int,

    @field:Schema(description = "단위")
    val unit: String
)

@Schema(description = "레시피 Step ResponseDTO")
data class StepResponse(
    @field:Schema(description = "제목")
    val title: String,

    @field:Schema(description = "상세 과정 목록")
    val processes: List<ProcessResponse>,
)

@Schema(description = "레시피 Step 과정 ResponseDTO")
data class ProcessResponse(
    @field:Schema(description = "내용")
    val content: String
)

data class UpdateRecipeRequest(
    @field:Schema(description = "레시피 제목")
    val title: String? = null,

    @field:Schema(description = "이미지 URL")
    val imageUrl: String? = null,

    @field:Schema(description = "용량")
    val servings: Int? = null,

    @field:Schema(description = "비용")
    val cost: Int? = null,

    @field:Schema(description = "요리 소요 시간 (분)")
    val cookingTime: Int? = null,

    @field:Schema(description = "메모")
    val memo: String? = null,

    @field:Schema(description = "기본 재료")
    val basicIngredients: List<IngredientRequest>? = null,

    @field:Schema(description = "소스 재료")
    val sourceIngredients: List<IngredientRequest>? = null,

    @field:Schema(description = "요리 단계 목록")
    val steps: List<StepRequest>? = null
) {
    fun toCommand(recipeId: String): UpdateRecipeCommand {
        return UpdateRecipeCommand(
            recipeId = ObjectId(recipeId),
            title = this.title,
            imageUrl = this.imageUrl,
            servings = this.servings,
            cost = this.cost,
            cookingTime = this.cookingTime,
            memo = this.memo,
            basicIngredients = this.basicIngredients?.map { it.toCommandInfo() },
            sourceIngredients = this.sourceIngredients?.map { it.toCommandInfo() },
            steps = this.steps?.map { it.toCommandInfo() }
        )
    }
}

data class DeleteRecipeRequest(
    @field:Schema(description = "레시피 ID")
    val recipeId: String
)

@Schema(description = "레시피 검색 필터")
data class RecipeSearchFilter(
    @field:Min(value = 0, message = "비용은 0 이상이어야 합니다.")
    @field:Schema(description = "최소 비용")
    val costGte: Int? = null,

    @field:Min(value = 0, message = "비용은 0 이상이어야 합니다.")
    @field:Schema(description = "최대 비용")
    val costLte: Int? = null,

    @field:Positive(message = "조리 시간은 양수여야 합니다.")
    @field:Schema(description = "최소 조리 시간")
    val cookingTimeGte: Int? = null,

    @field:Positive(message = "조리 시간은 양수여야 합니다.")
    @field:Schema(description = "최대 조리 시간")
    val cookingTimeLte: Int? = null,
)

@Schema(description = "레시피 목록 ResponseDTO with cursor")
data class RecipeSliceResponse(
    @field:Schema(description = "레시피 목록")
    val resultList: List<SimpleRecipeResponse>,

    @field:Schema(description = "다음 커서 ID")
    val nextCursorId: String? = null
)

@Schema(description = "레시피 기본 정보 ResponseDTO")
data class SimpleRecipeResponse(
    @field:Schema(description = "레시피 ID")
    val recipeId: String,

    @field:Schema(description = "레시피 등록 회원 ID")
    val authorId: String,

    @field:Schema(description = "레시피 등록 회원 닉네임")
    val authorName: String,

    @field:Schema(description = "레시피 제목")
    val title: String,

    @field:Schema(description = "이미지 URL")
    val imageUrl: String,

    @field:Schema(description = "용량")
    val servings: Int,

    @field:Schema(description = "비용")
    val cost: Int?,

    @field:Schema(description = "소요 시간 (분)")
    val cookingTime: Int?
)

@Schema(description = "S3 파일 업로드 URL RequestDTO")
data class FileUploadRequest(
    @field:Schema(description = "파일명")
    val fileName: String,

    @field:Schema(description = "ContentType (image/jpeg, image/png)")
    val contentType: String
)

enum class ContentType(val value: String) {
    JPEG("image/jpeg"),
    PNG("image/png")
    ;

    companion object {
        fun of(value: String): ContentType {
            return when (value) {
                JPEG.value -> JPEG
                PNG.value -> PNG
                else -> throw BaseException(ErrorCode.CONTENT_TYPE_INVALID)
            }
        }
    }
}

@Schema(description = "S3 파일 업로드 URL ResponseDTO")
data class FileUploadResponse(
    @field:Schema(description = "S3 업로드 요청 URL")
    val uploadUrl: String,

    @field:Schema(description = "S3 FileKey")
    val fileKey: String,

    @field:Schema(description = "이미지 URL")
    val imageUrl: String,

    @field:Schema(description = "업로드 요청 헤더 (요청 헤더에 모두 필수로 포함되어야 함)")
    val requiredHeaders: Map<String, String> = emptyMap()
)