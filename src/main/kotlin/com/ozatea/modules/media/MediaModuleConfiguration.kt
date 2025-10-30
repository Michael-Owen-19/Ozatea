package com.ozatea.modules.media

import com.ozatea.modules.media.domain.MediaStorage
import com.ozatea.modules.media.infrastructure.LocalMediaStorage
import com.ozatea.modules.media.infrastructure.S3MediaStorage
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MediaModuleConfiguration {

    @Value("\${media.storage.type:local}")
    private lateinit var storageType: String

    @Bean
    fun mediaStorage(
        localStorage: LocalMediaStorage,
        s3Storage: S3MediaStorage
    ): MediaStorage {
        return when (storageType.lowercase()) {
            "s3" -> s3Storage
            else -> localStorage
        }
    }
}