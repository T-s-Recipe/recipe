package shop.tsrecipe.recipe.config

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.ReactiveAuditorAware
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.index.IndexDefinition
import org.springframework.data.mongodb.core.index.TextIndexDefinition
import reactor.core.publisher.Mono

@EnableReactiveMongoAuditing
@Configuration
class MongoConfig(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) {
    @Bean
    fun reactiveAuditorAware(): ReactiveAuditorAware<String> {
        return ReactiveAuditorAware { Mono.empty() }
    }

    @PostConstruct
    fun initIndexes() {
        val textIndex: IndexDefinition = TextIndexDefinition.builder()
            .onField("title", 2.0f)
            .onField("basicIngredients.name", 1.5f)
            .onField("sourceIngredients.name", 1.0f)
            .withDefaultLanguage("english")
            .named("recipes_text_index")
            .build()

        reactiveMongoTemplate.indexOps("recipes")
            .createIndex(textIndex)
            .subscribe()
    }
}