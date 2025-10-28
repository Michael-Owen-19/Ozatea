package com.ozatea.modules.product.application

import com.ozatea.modules.category.domain.CategoryRepository
import com.ozatea.modules.product.domain.ProductRepository
import com.ozatea.modules.product.domain.event.ProductCreatedEvent
import com.ozatea.modules.product.domain.event.ProductEventPublisher
import com.ozatea.modules.product.infrastructure.ProductMapper
import com.ozatea.modules.product.presentation.ProductRequest
import com.ozatea.modules.product.presentation.ProductResponse
import jakarta.persistence.EntityNotFoundException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val repository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val eventPublisher: ProductEventPublisher
) {

    @Transactional
    fun create(request: ProductRequest): ProductResponse {
        val category = request.categoryId?.let {
            categoryRepository.findById(it)
                ?: throw EntityNotFoundException("Category not found: $it")
        }
        val product = ProductMapper.toEntity(request, category)
        val saved = repository.save(product)
        eventPublisher.publishProductCreatedEvent(ProductCreatedEvent.from(saved))
        return ProductMapper.toResponse(saved)
    }

    fun findAll(): List<ProductResponse> {
        return repository.findAll().map { ProductMapper.toResponse(it) }
    }

    fun findById(id: Long): ProductResponse {
        val product = repository.findById(id) ?: throw EntityNotFoundException("Product not found: $id")
        return ProductMapper.toResponse(product)
    }

    @Transactional
    fun deleteById(id: Long) {
        if (repository.findById(id) == null) {
            throw EntityNotFoundException("Product not found: $id")
        }
        repository.deleteById(id)
    }
}