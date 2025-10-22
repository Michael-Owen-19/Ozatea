package com.ozatea.modules.product.infrastructure

import com.ozatea.modules.product.domain.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaProductRepository : JpaRepository<Product, Long>
