package shop.tsrecipe.recipe.service

import org.bson.types.ObjectId
import shop.tsrecipe.recipe.domain.*

data class CreateRecipeCommand(
    val authorId: ObjectId,
    var authorNickname: String? = null,
    val title: String,
    val imageUrl: String,
    val servings: Int,
    val cost: Int?,
    val cookingTime: Int?,
    val memo: String?,
    val basicIngredients: List<IngredientInfo>,
    val sourceIngredients: List<IngredientInfo>,
    val steps: List<StepInfo>
) {
    fun setNickname(nickname: String) {
        this.authorNickname = nickname
    }
}

data class IngredientInfo(
    val name: String,
    val measurements: List<MeasurementInfo>
) {
    fun toEntity(): Ingredient {
        return Ingredient(
            name = this.name,
            measurements = this.measurements.map { it.toEntity() }
        )
    }
}

data class MeasurementInfo(
    val amount: Int,
    val unit: IngredientUnit
) {
    fun toEntity(): Measurement {
        return Measurement(this.amount, this.unit)
    }
}

data class StepInfo(
    val title: String,
    val steps: List<ProcessInfo>
) {
    fun toEntity(): Step {
        return Step(
            title = this.title,
            processes = this.steps.map { it.toEntity() }
        )
    }
}

data class ProcessInfo(
    val content: String,
    val imageUrl: String?
) {
    fun toEntity(): Process {
        return Process(
            content = content,
            imageUrl = imageUrl
        )
    }
}

data class GetRecentCommand(
    val limit: Int,
    val cursorId: ObjectId?
)