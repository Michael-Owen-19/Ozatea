package com.ozatea.modules.product_media.application

import com.ozatea.modules.product_media.domain.ProductMedia
import com.ozatea.modules.product_media.domain.ProductMediaRepository
import com.ozatea.modules.product_media.presentation.ProductMediaResponse
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductMediaService(
    private val productMediaRepository: ProductMediaRepository,
) {
    @Transactional
    fun create(productMedia: ProductMedia): ProductMediaResponse {
        val savedProductMedia = productMediaRepository.save(productMedia)
        return ProductMediaResponse.from(savedProductMedia)
    }

    fun findById(id: Long): ProductMediaResponse {
        val productMedia = productMediaRepository.findById(id)
            ?: throw EntityNotFoundException("ProductMedia with id $id not found")
        return ProductMediaResponse.from(productMedia)
    }
}