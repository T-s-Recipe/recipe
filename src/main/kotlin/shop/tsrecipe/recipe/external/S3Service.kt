package shop.tsrecipe.recipe.external

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import shop.tsrecipe.recipe.api.ContentType
import shop.tsrecipe.recipe.api.FileUploadResponse
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest
import java.time.Duration
import java.util.*

@Service
class S3Service(
    private val s3Presigner: S3Presigner,
    @Value("\${aws.bucketName}") private val bucketName: String,
    @Value("\${aws.region}") private val region: String,
) {
    suspend fun getPresignedUrl(fileName: String, contentType: ContentType): FileUploadResponse {
        val sanitizedFilename = fileName.sanitizeFileName()
        val fileKey = "${UUID.randomUUID()}-${sanitizedFilename}"

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileKey)
            .contentType(contentType.value)
            .build()

        val presignedRequest: PresignedPutObjectRequest = s3Presigner.presignPutObject {
            it.putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofMinutes(5))
        }

        val signedHeaders: Map<String, String> =
            presignedRequest.httpRequest().headers().mapValues { it.value.joinToString(",") }

        return FileUploadResponse(
            uploadUrl = presignedRequest.url().toString(),
            fileKey = fileKey,
            imageUrl = "https://$bucketName.s3.$region.amazonaws.com/$fileKey",
            requiredHeaders = signedHeaders,
        )
    }

    fun String.sanitizeFileName(): String {
        val baseName = this.substringBeforeLast(".")
        val extension = this.substringAfterLast(".", "")

        var sanitizedBaseName = baseName
            .replace(Regex("[^a-zA-Z0-9가-힣_-]"), " ")
            .trim()
            .replace(Regex("\\s+"), "-")
            .lowercase()

        if (sanitizedBaseName.isEmpty()) {
            sanitizedBaseName = "file"
        }

        return if (extension.isNotEmpty()) {
            "$sanitizedBaseName.$extension"
        } else {
            sanitizedBaseName
        }
    }
}