package com.ozatea.modules.media.infrastructure

import com.ozatea.modules.media.domain.MediaStorage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@Component
class LocalMediaStorage(
    @Value("\${media.local.path:storage}") private val basePath: String
) : MediaStorage {

    override fun saveFile(inputStream: InputStream, filename: String, mimeType: String): String {
        val folder = File(basePath)
        if (!folder.exists()) folder.mkdirs()

        val file = File(folder, filename)
        Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING)

        // return relative URL
        return "${file.toPath()}"
    }

    override fun deleteFile(filename: String) {
        val file = File(basePath, filename)
        if(!file.exists()) {
            throw RuntimeException("File not found: $filename")
        }
        if (!file.delete()) {
            throw RuntimeException("Failed to delete file: $filename")
        }
    }
}