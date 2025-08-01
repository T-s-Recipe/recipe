package shop.tsrecipe.recipe.api

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Positive
import org.bson.types.ObjectId
import shop.tsrecipe.recipe.domain.IngredientUnit
import shop.tsrecipe.recipe.service.*

@Schema(description = "레시피 등록 RequestDTO")
data class CreateRecipeRequest(
    @field:Schema(description = "레시피 등록 회원 ID")
    val authorId: String,

    @field:Schema(description = "레시피 제목")
    val title: String,

    @field:Schema(description = "용량 (defaultValue: 4)")
    val servings: Int = 4,

    @field:Schema(description = "비용")
    val cost: Int? = null,

    @field:Schema(description = "소요 시간 (분)")
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
    fun toCommand(): CreateRecipeCommand {
        return CreateRecipeCommand(
            authorId = ObjectId(this.authorId),
            title = this.title,
            servings = this.servings,
            cost = this.cost,
            cookingTime = this.cookingTime,
            memo = this.memo,
            basicIngredients = this.basicIngredients.map { it.toCommandInfo() },
            sourceIngredients = this.sourceIngredients.map { it.toCommandInfo() },
            steps = this.steps.map { it.toCommandInfo() }
        )
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
                name = this.name,
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
        val process: List<ProcessRequest>,
    ) {
        fun toCommandInfo(): StepInfo {
            return StepInfo(
                title = this.title,
                steps = this.process.map { it.toCommandInfo() }
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
    val steps: List<ProcessResponse>,
)

@Schema(description = "레시피 Step 과정 ResponseDTO")
data class ProcessResponse(
    @field:Schema(description = "내용")
    val content: String
)

@Schema(description = "레시피 검색 상세 조건")
data class RecipeSearchCondition(
    @field:Min(value = 0, message = "비용은 0 이상이어야 합니다.")
    @field:Schema(description = "비용 동등 조건(==)")
    val costEq: Int?,

    @field:Min(value = 0, message = "최소 비용은 0 이상이어야 합니다.")
    @field:Schema(description = "비용 최소 조건 (>=)")
    val costGte: Int?,

    @field:Min(value = 0, message = "최대 비용은 0 이상이어야 합니다.")
    @field:Schema(description = "비용 최대 조건 (<=)")
    val costLte: Int?,

    @field:Positive(message = "조리 시간은 양수여야 합니다.")
    @field:Schema(description = "조리 시간 최소 조건 (>=)")
    val cookingTimeGte: Int?,

    @field:Positive(message = "조리 시간은 양수여야 합니다.")
    @field:Schema(description = "조리 시간 최대 조건 (<=)")
    val cookingTimeLte: Int?,
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
