package shop.tsrecipe.recipe.service

import shop.tsrecipe.recipe.domain.Ingredient
import shop.tsrecipe.recipe.domain.IngredientUnit
import shop.tsrecipe.recipe.domain.Section
import shop.tsrecipe.recipe.domain.Step
import org.bson.types.ObjectId

data class CreateRecipeCommand(
    val authorId: ObjectId,
    val name: String,
    val imageUrl: String,
    val servings: Int,
    val cost: Int,
    val cookingTime: Int,
    val ingredients: List<IngredientInfo>,
    val sections: List<SectionInfo>,
    val description: String?,
)

data class IngredientInfo(
    val name: String,
    val amount: Int,
    val unit: IngredientUnit
) {
    fun toEntity(): Ingredient {
        return Ingredient(
            name = this.name,
            amount = this.amount,
            unit = this.unit
        )
    }
}

data class SectionInfo(
    val title: String,
    val steps: List<StepInfo>
) {
    fun toEntity(): Section {
        return Section(
            title = this.title,
            steps = this.steps.map { it.toEntity() }
        )
    }
}

data class StepInfo(
    val content: String,
    val imageUrl: String
) {
    fun toEntity(): Step {
        return Step(
            content = this.content,
            imageUrl = this.imageUrl
        )
    }
}