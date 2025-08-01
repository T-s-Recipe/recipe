package shop.tsrecipe.recipe.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient

@Configuration
class S3Config(
    @Value("\${aws.access-key}") private val accessKey: String,
    @Value("\${aws.secret-key}") private val secretKey: String,
    @Value("\${aws.region}") private val region: String
) {
    @Bean
    fun s3AsyncClient(): S3AsyncClient {
        val credentials = AwsBasicCredentials.create(accessKey, secretKey)
        return S3AsyncClient.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()
    }
}