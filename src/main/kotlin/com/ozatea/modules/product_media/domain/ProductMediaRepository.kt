package com.ozatea.modules.product_media.domain

interface ProductMediaRepository {
    fun save(productMedia: ProductMedia): ProductMedia
    fun findById(id: Long): ProductMedia?
    fun delete(productMedia: ProductMedia)
    fun deleteById(id: Long)
}