package shop.tsrecipe.recipe.external.member

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import shop.tsrecipe.recipe.exception.BaseException
import shop.tsrecipe.recipe.exception.ErrorCode
import shop.tsrecipe.recipe.external.exception.ExternalServiceException
import shop.tsrecipe.recipe.external.exception.getLogMessage
import shop.tsrecipe.recipe.util.Logging

@Service
class MemberService(
    private val memberClient: MemberClient
) : Logging {

    @CircuitBreaker(name = "member-service", fallbackMethod = "getMemberFailed")
    suspend fun getMemberById(memberId: String): MemberResponse? {
        return memberClient.getMemberById(memberId).awaitSingleOrNull()
    }

    private suspend fun getMemberFailed(throwable: Throwable) {
        val defaultMessage = "[fallback method]: getMemberFailed.\n"
        when (throwable) {
            is ExternalServiceException -> {
                logger.error { "$defaultMessage ${throwable.getLogMessage()}" }
                throw BaseException(throwable)
            }

            else -> {
                logger.error { "$defaultMessage undefined error." }
                throw BaseException(ErrorCode.UNDEFINED_EXCEPTION)
            }
        }
    }
}