package shop.tsrecipe.recipe.external

import kotlinx.coroutines.future.await
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import shop.tsrecipe.recipe.exception.BaseException
import shop.tsrecipe.recipe.exception.ErrorCode
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

@Service
class S3Service(
    private val s3AsyncClient: S3AsyncClient,
    @Value("\${aws.bucketName}") private val bucketName: String
) {
    suspend fun upload(file: FilePart): String {
        val tempFile: Path = Files.createTempFile("s3-upload-", file.filename())

        try {
            file.transferTo(tempFile).awaitFirstOrNull()

            val fileLength = Files.size(tempFile)

            val sanitizedFilename = file.filename().sanitizeFileName()
            val fileKey = "${UUID.randomUUID()}-${sanitizedFilename}"

            val putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .contentType(file.headers().contentType?.toString())
                .contentLength(fileLength)
                .build()

            val requestBody = AsyncRequestBody.fromFile(tempFile)

            s3AsyncClient.putObject(putObjectRequest, requestBody).await()

            return s3AsyncClient.utilities().getUrl { builder -> builder.bucket(bucketName).key(fileKey) }.toExternalForm()

        } finally {
            try {
                Files.deleteIfExists(tempFile)
            } catch (e: Exception) {
                throw BaseException(ErrorCode.FILE_UPLOAD_FAILED)
            }
        }
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