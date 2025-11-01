package com.ozatea.modules.product.infrastructure

import com.ozatea.modules.product.domain.Product
import com.ozatea.modules.product.domain.ProductRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class ProductRepositoryImpl(
    private val jpaRepository: JpaProductRepository
) : ProductRepository {

    override fun findById(id: Long): Product? =
        jpaRepository.findByIdOrNull(id)

    override fun save(product: Product): Product =
        jpaRepository.save(product)

    override fun findAll(pageable: Pageable): Page<Product> =
        jpaRepository.findAll(pageable)

    override fun deleteById(id: Long) {
        jpaRepository.deleteById(id)
    }
}
