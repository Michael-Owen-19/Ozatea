package com.ozatea.modules.media.application

import com.ozatea.core.enums.MediaType
import com.ozatea.core.enums.StorageType
import com.ozatea.modules.media.domain.Media
import com.ozatea.modules.media.domain.MediaRepository
import com.ozatea.modules.media.domain.MediaStorage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class MediaService(
    private val mediaStorage: MediaStorage,
    private val mediaRepository: MediaRepository,
    @Value("\${media.storage.type:local}") private val storageType: String
) {
    fun getById(id: Long): Media? {
        return mediaRepository.findById(id)
    }

    fun loadFileAsBytes(url: String): ByteArray {
        val file = File(url)
        if (!file.exists()) throw RuntimeException("File not found at $url")
        return file.readBytes()
    }

    fun upload(file: MultipartFile, altText: String?): Media {
        val mimeType = file.contentType ?: detectMimeType(file.originalFilename)
        val mediaType = detectMediaType(mimeType)
        val filename = generateFilename(file.originalFilename)

        // save file physically
        val url = mediaStorage.saveFile(file.inputStream, filename, mimeType)

        val media = Media(
            filename = filename,
            originalFilename = file.originalFilename ?: filename,
            altText = altText,
            mimeType = mimeType,
            mediaType = mediaType,
            size = file.size,
            url = url,
            storageType = storageType.uppercase(Locale.getDefault()).let { StorageType.valueOf(it) }
        )

        return mediaRepository.save(media)
    }

    fun delete(id: Long) {
        val media = mediaRepository.findById(id) ?: return
        mediaStorage.deleteFile(media.filename)
        mediaRepository.delete(id)
    }

    private fun detectMimeType(filename: String?): String {
        if (filename == null) return "application/octet-stream"
        val path = Paths.get(filename)
        return Files.probeContentType(path) ?: "application/octet-stream"
    }

    private fun detectMediaType(mimeType: String): MediaType {
        return when {
            mimeType.startsWith("image/") -> MediaType.IMAGE
            mimeType.startsWith("video/") -> MediaType.VIDEO
            else -> MediaType.OTHER
        }
    }

    private fun generateFilename(originalName: String?): String {
        val ext = originalName?.substringAfterLast('.', "") ?: ""
        val uuid = UUID.randomUUID().toString()
        return if (ext.isNotEmpty()) "$uuid.$ext" else uuid
    }
}
