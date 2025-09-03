package shop.tsrecipe.recipe.service

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.query.Criteria
import shop.tsrecipe.recipe.domain.*

data class CreateRecipeCommand(
    val authorId: ObjectId,
    var authorNickname: String? = null,
    val title: String,
    var imageUrl: String,
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
    val processes: List<ProcessInfo>
) {
    fun toEntity(): Step {
        return Step(
            title = this.title,
            processes = this.processes.map { it.toEntity() }
        )
    }
}

data class ProcessInfo(
    val content: String
) {
    fun toEntity(): Process {
        return Process(
            content = content
        )
    }
}

data class GetRecentCommand(
    val limit: Int,
    val cursorId: ObjectId?
)

data class SearchRecipeCommand(
    val authorId: ObjectId?,
    val nickname: String?,
    val searchKeyword: String?,
    val costGte: Int?,
    val costLte: Int?,
    val cookingTimeGte: Int?,
    val cookingTimeLte: Int?,
    val limit: Int,
    val cursorId: ObjectId?
) {
    fun toCriteria(): Criteria? {
        val criteriaList = mutableListOf<Criteria>()

        authorId?.let { criteriaList.add(Criteria.where("authorId").`is`(it)) }
        nickname?.let { criteriaList.add(Criteria.where("authorNickname").`is`(it)) }

        costGte?.let { criteriaList.add(Criteria.where("cost").gte(it)) }
        costLte?.let { criteriaList.add(Criteria.where("cost").lte(it)) }
        cookingTimeGte?.let { criteriaList.add(Criteria.where("cookingTime").gte(it)) }
        cookingTimeLte?.let { criteriaList.add(Criteria.where("cookingTime").lte(it)) }

        return if (criteriaList.isEmpty()) null else Criteria().andOperator(criteriaList)
    }
}