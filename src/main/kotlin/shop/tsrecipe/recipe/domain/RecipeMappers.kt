package shop.tsrecipe.recipe.domain

import shop.tsrecipe.recipe.api.IngredientResponse
import shop.tsrecipe.recipe.api.RecipeResponse
import shop.tsrecipe.recipe.api.SectionResponse
import shop.tsrecipe.recipe.api.StepResponse

fun Recipe.toResponse(): RecipeResponse {
    return RecipeResponse(
        id = this.id.toString(),
        authorId = this.authorId.toString(),
        name = this.name,
        imageUrl = this.imageUrl,
        servings = this.servings,
        cost = this.cost,
        cookingTime = this.cookingTime,
        ingredients = this.ingredients.map { it.toResponse() },
        sections = this.sections.map { it.toResponse() },
        description = this.description,
    )
}

fun Ingredient.toResponse(): IngredientResponse {
    return IngredientResponse(
        name = this.name,
        amount = this.amount,
        unit = this.unit.value
    )
}

fun Section.toResponse(): SectionResponse {
    return SectionResponse(
        title = this.title,
        steps = this.steps.map { it.toResponse() },
    )
}

fun Step.toResponse(): StepResponse {
    return StepResponse(
        content = this.content,
        imageUrl = this.imageUrl,
    )
}