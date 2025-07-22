package shop.tsrecipe.recipe.api

import shop.tsrecipe.recipe.domain.IngredientUnit
import shop.tsrecipe.recipe.service.CreateRecipeCommand
import shop.tsrecipe.recipe.service.IngredientInfo
import shop.tsrecipe.recipe.service.SectionInfo
import shop.tsrecipe.recipe.service.StepInfo
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Positive
import org.bson.types.ObjectId

@Schema(description = "레시피 등록 RequestDTO")
data class CreateRecipeRequest(
    @field:Schema(description = "레시피 등록 회원 ID")
    val authorId: String,

    @field:Schema(description = "요리 이름")
    val name: String,

    @field:Schema(description = "이미지 URL")
    val imageUrl: String,

    @field:Schema(description = "용량")
    val servings: Int,

    @field:Schema(description = "비용")
    val cost: Int,

    @field:Schema(description = "소요 시간 (분)")
    val cookingTime: Int,

    @field:Schema(description = "재료 목록")
    val ingredients: List<IngredientRequest>,

    @field:Schema(description = "요리 단계 목록")
    val sections: List<SectionRequest>,

    @field:Schema(description = "설명")
    val description: String?
) {
    fun toCommand(): CreateRecipeCommand {
        return CreateRecipeCommand(
            authorId = ObjectId(this.authorId),
            name = this.name,
            imageUrl = this.imageUrl,
            servings = this.servings,
            cost = this.cost,
            cookingTime = this.cookingTime,
            ingredients = this.ingredients.map { it.toCommandInfo() },
            sections = this.sections.map { it.toCommandInfo() },
            description = this.description,
        )
    }

    @Schema(description = "재료 RequestDTO")
    data class IngredientRequest(
        @field:Schema(description = "재료 이름")
        val name: String,

        @field:Schema(description = "양")
        val amount: Int,

        @field:Schema(description = "단위 (COUNT / GRAM / MILLILITER / TABLESPOON / TEASPOON)")
        val unit: IngredientUnit
    ) {
        fun toCommandInfo(): IngredientInfo {
            return IngredientInfo(
                name = this.name,
                amount = this.amount,
                unit = this.unit
            )
        }
    }

    @Schema(description = "요리 단계 RequestDTO")
    data class SectionRequest(
        @field:Schema(description = "단계 이름")
        val title: String,

        @field:Schema(description = "요리 과정 목록")
        val steps: List<StepRequest>,
    ) {
        fun toCommandInfo(): SectionInfo {
            return SectionInfo(
                title = this.title,
                steps = this.steps.map { it.toCommandInfo() }
            )
        }
    }

    @Schema(description = "요리 과정 RequestDTO")
    data class StepRequest(
        @field:Schema(description = "요리 과정 내용")
        val content: String,

        @field:Schema(description = "이미지 URL")
        val imageUrl: String
    ) {
        fun toCommandInfo(): StepInfo {
            return StepInfo(
                content = this.content,
                imageUrl = this.imageUrl
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

    @field:Schema(description = "요리 이름")
    val name: String,

    @field:Schema(description = "이미지 URL")
    val imageUrl: String,

    @field:Schema(description = "용량")
    val servings: Int,

    @field:Schema(description = "비용")
    val cost: Int,

    @field:Schema(description = "소요 시간 (분)")
    val cookingTime: Int,

    @field:Schema(description = "재료 목록")
    val ingredients: List<IngredientResponse>,

    @field:Schema(description = "요리 단계 목록")
    val sections: List<SectionResponse>,

    @field:Schema(description = "설명")
    val description: String?
)

@Schema(description = "재료 ResponseDTO")
data class IngredientResponse(
    @field:Schema(description = "재료 이름")
    val name: String,

    @field:Schema(description = "양")
    val amount: Int,

    @field:Schema(description = """
        재료의 단위를 나타냅니다.
        **(우측 문자열로 응답)**
        
        * `COUNT`: 개
        * `GRAM`: 그램 (g)
        * `MILLILITER`: 밀리리터 (ml)
        * `TABLESPOON`: 큰술 (T)
        * `TEASPOON`: 작은술 (t)
        
        """)
    val unit: String
)

@Schema(description = "요리 단계 ResponseDTO")
data class SectionResponse(
    @field:Schema(description = "단계 이름")
    val title: String,

    @field:Schema(description = "요리 과정 목록")
    val steps: List<StepResponse>,
)

@Schema(description = "요리 과정 ResponseDTO")
data class StepResponse(
    @field:Schema(description = "요리 과정 내용")
    val content: String,

    @field:Schema(description = "이미지 URL")
    val imageUrl: String
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