package shop.tsrecipe.recipe.domain

import shop.tsrecipe.recipe.api.*

fun Recipe.toResponse(): RecipeResponse {
    return RecipeResponse(
        id = this.id.toString(),
        authorId = this.authorId.toString(),
        authorNickname = this.authorNickname,
        title = this.title,
        imageUrl = this.imageUrl,
        servings = this.servings,
        cost = this.cost,
        cookingTime = this.cookingTime,
        memo = this.memo,
        basicIngredients = this.basicIngredients.map { it.toResponse() },
        sourceIngredients = this.sourceIngredients.map { it.toResponse() },
        steps = this.steps.map { it.toResponse() }
    )
}

fun List<Recipe>.toSliceResponse(): RecipeSliceResponse {
    val nextCursor = this.lastOrNull()

    return RecipeSliceResponse(
        resultList = this.map { it.toSimpleResponse() },
        nextCursorId = nextCursor?.id?.toString()
    )
}

fun Recipe.toSimpleResponse(): SimpleRecipeResponse {
    return SimpleRecipeResponse(
        authorId = this.authorId.toString(),
        authorName = this.authorNickname,
        imageUrl = this.imageUrl,
        title = this.title,
        servings = this.servings,
        cost = this.cost,
        cookingTime = this.cookingTime,
    )
}

fun Ingredient.toResponse(): IngredientResponse {
    return IngredientResponse(
        name = this.name,
        measurements = this.measurements.map { it.toResponse() }
    )
}

fun Measurement.toResponse(): MeasurementResponse {
    return MeasurementResponse(
        amount = this.amount,
        unit = this.unit.value
    )
}

fun Step.toResponse(): StepResponse {
    return StepResponse(
        title = this.title,
        steps = this.processes.map { it.toResponse() },
    )
}

fun Process.toResponse(): ProcessResponse {
    return ProcessResponse(
        content = this.content,
        imageUrl = this.imageUrl,
    )
}