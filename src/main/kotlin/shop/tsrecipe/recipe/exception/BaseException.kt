package shop.tsrecipe.recipe.exception

import org.springframework.http.HttpStatusCode
import shop.tsrecipe.recipe.external.exception.ExternalServiceException
import shop.tsrecipe.recipe.util.getCurrentTimestamp

class BaseException(
    val httpStatus: HttpStatusCode,
    val code: String? = null,
    override val message: String,
) : RuntimeException() {

    val timestamp = getCurrentTimestamp()

    constructor(e: ErrorCode) : this(
        httpStatus = e.httpStatus,
        code = e.name,
        message = e.message,
    )

    constructor(e: ExternalServiceException) : this(
        httpStatus = e.httpStatus,
        code = e.code,
        message = e.message
    )
}