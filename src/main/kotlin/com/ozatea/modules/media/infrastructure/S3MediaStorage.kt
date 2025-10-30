package com.ozatea.modules.media.infrastructure

import com.ozatea.modules.media.domain.MediaStorage
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
class S3MediaStorage : MediaStorage {
    override fun saveFile(inputStream: InputStream, filename: String, mimeType: String): String {
        // TODO: Implement AWS SDK S3 upload
        throw NotImplementedError("S3 storage not yet configured")
    }

    override fun deleteFile(filename: String) {
        // TODO: Implement S3 delete
    }
}
