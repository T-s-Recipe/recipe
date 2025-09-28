package shop.tsrecipe.recipe.annotation

import org.bson.types.ObjectId
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import shop.tsrecipe.recipe.exception.BaseException
import shop.tsrecipe.recipe.exception.ErrorCode

@Component
class MemberIdArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(MemberId::class.java)
                && parameter.parameterType == ObjectId::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any> {
        val authHeaderValue = exchange.request.headers.getFirst(HeaderNames.X_MEMBER_ID)

        if (authHeaderValue.isNullOrBlank()) {
            return Mono.error(BaseException(ErrorCode.MEMBER_HEADER_MISSING))
        }

        return Mono.just(ObjectId(authHeaderValue))
    }
}