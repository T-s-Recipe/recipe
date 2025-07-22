package shop.tsrecipe.recipe.domain

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "recipes")
data class Recipe(
    @Id
    val id: ObjectId = ObjectId.get(),
    val authorId: ObjectId,
    val name: String,
    val imageUrl: String,
    val servings: Int,
    val cost: Int,
    val cookingTime: Int,
    val ingredients: List<Ingredient> = emptyList(),
    val sections: List<Section> = emptyList(),
    val description: String?
): Auditable()

data class Ingredient(
    val name: String,
    val amount: Int,
    val unit: IngredientUnit
)

enum class IngredientUnit(val value: String) {
    COUNT("개"),
    GRAM("g"),
    MILLILITER("ml"),
    TABLESPOON("T"),
    TEASPOON("t")
}

data class Section(
    val title: String,
    val steps: List<Step>
)

data class Step(
    val content: String,
    val imageUrl: String
)
