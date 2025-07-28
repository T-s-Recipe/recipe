package shop.tsrecipe.recipe.external.member

import com.fasterxml.jackson.databind.ObjectMapper
import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.http.HttpStatus
import shop.tsrecipe.recipe.exception.BaseException
import shop.tsrecipe.recipe.exception.ErrorResponse
import shop.tsrecipe.recipe.external.exception.ExternalServiceException.*

class MemberErrorDecoder(
    private val objectMapper: ObjectMapper
) : ErrorDecoder {
    private val serviceName: String = "member"

    override fun decode(methodKey: String, response: Response): Exception {
        val httpStatus = HttpStatus.resolve(response.status()) ?: HttpStatus.INTERNAL_SERVER_ERROR

        if (httpStatus.is5xxServerError) {
            return ExternalServiceUnavailable(serviceName)
        }

        val errorResponse: ErrorResponse = try {
            objectMapper.readValue(response.body().asInputStream(), ErrorResponse::class.java)
        } catch (e: Exception) {
            return ExternalResponseParsingException("Response parsing error from $serviceName-service.")
        }

        return BaseException(
            httpStatus = httpStatus,
            code = errorResponse.code,
            message = errorResponse.message
        )
    }
}