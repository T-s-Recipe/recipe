package shop.tsrecipe.recipe.service

import kotlinx.coroutines.reactor.awaitSingle
import shop.tsrecipe.recipe.domain.Recipe
import shop.tsrecipe.recipe.util.buildQueryById
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.bson.types.ObjectId
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.data.mongodb.core.query.TextQuery
import org.springframework.stereotype.Repository

@Repository
class RecipeReader(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) {
    suspend fun findOneById(recipeId: ObjectId): Recipe? {
        return reactiveMongoTemplate.findOne(
            buildQueryById(recipeId),
            Recipe::class.java
        ).awaitSingleOrNull()
    }

    suspend fun findAllByCursor(command: GetRecentCommand): List<Recipe> {
        val query = Query()

        query.addPagingOptions(command.cursorId, command.limit)

        return reactiveMongoTemplate.find(
            query,
            Recipe::class.java
        ).collectList().awaitSingle().toList()
    }

    private suspend fun Query.addPagingOptions(cursorId: ObjectId?, limit: Int) {
        apply {
            cursorId?.let {
                this.addCriteria(Criteria.where("_id").lt(it))
            }

            with(Sort.by(Sort.Direction.DESC, "_id"))
                .limit(limit)
        }
    }

    suspend fun findAllBySearchConditions(command: SearchRecipeCommand): List<Recipe> {
        val query = if (!command.searchKeyword.isNullOrBlank()) {
            val textCriteria = TextCriteria.forDefaultLanguage().matching(command.searchKeyword)
            TextQuery.query(textCriteria).with(Sort.by(Sort.Direction.DESC, "score"))
        } else Query()

        command.toCriteria()?.let { query.addCriteria(it) }

        query.addPagingOptions(command.cursorId, command.limit)

        return reactiveMongoTemplate.find(
            query, Recipe::class.java
        ).collectList().awaitSingle()
    }
}