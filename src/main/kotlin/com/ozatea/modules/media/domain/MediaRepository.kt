package com.ozatea.modules.media.domain

interface MediaRepository {
    fun save(media: Media): Media
    fun findById(id: Long): Media?
    fun delete(id: Long)
}