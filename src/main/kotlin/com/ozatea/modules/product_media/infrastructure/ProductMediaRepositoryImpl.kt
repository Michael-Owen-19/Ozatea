package com.ozatea.modules.product_media.infrastructure

import com.ozatea.modules.product_media.domain.ProductMedia
import com.ozatea.modules.product_media.domain.ProductMediaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class ProductMediaRepositoryImpl(
    private val jpaRepository: JpaProductMediaRepository
) : ProductMediaRepository {
    override fun findById(id: Long): ProductMedia? =
        jpaRepository.findByIdOrNull(id)

    override fun save(productMedia: ProductMedia): ProductMedia =
        jpaRepository.save(productMedia)

    override fun delete(productMedia: ProductMedia) =
        jpaRepository.delete(productMedia)

    override fun deleteById(id: Long) =
        jpaRepository.deleteById(id)

}