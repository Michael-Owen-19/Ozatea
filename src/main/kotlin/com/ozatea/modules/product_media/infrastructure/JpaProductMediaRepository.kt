package com.ozatea.modules.product_media.infrastructure

import com.ozatea.modules.product_media.domain.ProductMedia
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaProductMediaRepository : JpaRepository<ProductMedia, Long>