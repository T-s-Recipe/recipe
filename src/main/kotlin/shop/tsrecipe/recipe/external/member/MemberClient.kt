package shop.tsrecipe.recipe.external.member

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import reactivefeign.spring.config.ReactiveFeignClient
import reactor.core.publisher.Mono
import shop.tsrecipe.recipe.config.MemberFeignConfig

@ReactiveFeignClient(
    name = "member-service",
    url = "\${external-service-url.member}",
    configuration = [MemberFeignConfig::class]
)
interface MemberClient {
    @GetMapping
    fun getMemberById(@RequestParam memberId: String): Mono<MemberResponse>
}