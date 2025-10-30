package com.ozatea.modules.media.presentation

import com.ozatea.core.response.ApiResponse
import com.ozatea.modules.media.application.MediaService
import com.ozatea.modules.media.domain.Media
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.NoSuchFileException

@RestController
@RequestMapping("/media")
class MediaController(
    private val mediaService: MediaService
) {
    @PostMapping("/upload")
    fun upload(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("alt_text") altText: String
    ): ResponseEntity<Media> {
        return ResponseEntity.ok(mediaService.upload(file, altText))
    }

    @PostMapping("/bulk-upload", consumes = ["multipart/form-data"])
    fun bulkUpload(
        @ModelAttribute request: BulkMediaRequest
    ): ResponseEntity<List<Media>> {
        val uploadedMedia = request.media.map {
            mediaService.upload(it.file, it.altText)
        }
        return ResponseEntity.ok(uploadedMedia)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<ApiResponse<Unit>> {
        mediaService.delete(id)
        return ResponseEntity.ok(ApiResponse.success(message = "Media deleted successfully"))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<ByteArray> {
        val media = mediaService.getById(id) ?: throw NoSuchFileException("Media not found")
        val fileBytes = mediaService.loadFileAsBytes(media.filepath)
        val contentType = media.mimeType

        return ResponseEntity.ok()
            .header("Content-Type", contentType)
            .header("Content-Disposition", "attachment; filename=\"${media.originalFilename}\"")
            .body(fileBytes)
    }
}