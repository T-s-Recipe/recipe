package shop.tsrecipe.recipe.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

enum class ErrorCode(val httpStatus: HttpStatusCode = HttpStatus.NOT_FOUND, val message: String) {
    UNDEFINED_EXCEPTION(httpStatus = HttpStatus.INTERNAL_SERVER_ERROR, message = "Sorry, undefined exception"),
    BAD_REQUEST(httpStatus = HttpStatus.BAD_REQUEST, message = "Bad request. check API documents."),

    // recipe
    RECIPE_NOT_FOUND(message = "recipe not found.")
}