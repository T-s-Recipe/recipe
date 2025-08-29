package shop.tsrecipe.recipe.exception

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import shop.tsrecipe.recipe.util.baseResponse

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    private val externalLogFormat = "-----Error occurred from {}-----\n ErrorCode: {}, HttpStatus: {}, Details: '{}'"

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<ErrorResponse> {
        return baseResponse(e.httpStatus, ErrorResponse(e))
    }

}