package shop.tsrecipe.recipe.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import shop.tsrecipe.recipe.external.member.MemberErrorDecoder

@Configuration
class MemberFeignConfig {
    @Bean
    fun memberErrorDecoder(objectMapper: ObjectMapper): MemberErrorDecoder {
        return MemberErrorDecoder(objectMapper)
    }
}