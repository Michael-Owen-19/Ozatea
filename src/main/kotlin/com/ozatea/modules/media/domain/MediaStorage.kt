package com.ozatea.modules.media.domain

import java.io.InputStream

interface MediaStorage {
    fun saveFile(inputStream: InputStream, filename: String, mimeType: String): String
    fun deleteFile(filename: String)
}