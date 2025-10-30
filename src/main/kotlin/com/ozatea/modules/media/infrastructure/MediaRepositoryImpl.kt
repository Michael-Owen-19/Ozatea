package com.ozatea.modules.media.infrastructure

import com.ozatea.modules.media.domain.Media
import com.ozatea.modules.media.domain.MediaRepository
import org.springframework.stereotype.Repository

@Repository
class MediaRepositoryImpl(
    private val jpaRepository: JpaMediaRepository
) : MediaRepository {

    override fun save(media: Media): Media = jpaRepository.save(media)

    override fun findById(id: Long): Media? =
        jpaRepository.findById(id).orElse(null)

    override fun delete(id: Long) {
        jpaRepository.deleteById(id)
    }
}