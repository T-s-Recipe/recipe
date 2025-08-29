package shop.tsrecipe.recipe.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "external-service-url")
data class ExternalServiceUrl(
    var member: String = ""
)