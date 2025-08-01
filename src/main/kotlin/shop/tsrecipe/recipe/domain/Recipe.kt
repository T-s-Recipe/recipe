package shop.tsrecipe.recipe.domain

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "recipes")
data class Recipe(
    @Id
    val id: ObjectId? = null,
    val authorId: ObjectId,
    val authorNickname: String,
    val title: String,
    val imageUrl: String,
    val servings: Int,
    val cost: Int? = null,
    val memo: String? = null,
    val cookingTime: Int? = null,
    val basicIngredients: List<Ingredient> = emptyList(),
    val sourceIngredients: List<Ingredient> = emptyList(),
    val steps: List<Step> = emptyList()
): Auditable()

data class Ingredient(
    val name: String,
    val measurements: List<Measurement>
)

data class Measurement(
    val amount: Int,
    val unit: IngredientUnit
)

enum class IngredientUnit(val value: String) {
    GRAM("g"),
    MILLILITER("ml"),
    TABLESPOON("Tbsp"),
    TEASPOON("tsp"),
    CUP("cup"),
    OZ("oz"),
    QUANTITY("Qty")
}

data class Step(
    val title: String,
    val processes: List<Process>
)

// TODO 이미지 추가될 수도
data class Process(
    val content: String,
)
