package com.ozatea.modules.category.application

import com.ozatea.modules.category.domain.CategoryRepository
import com.ozatea.modules.category.domain.event.CategoryCreatedEvent
import com.ozatea.modules.category.domain.event.CategoryEventPublisher
import com.ozatea.modules.category.infrastructure.CategoryMapper
import com.ozatea.modules.category.presentation.CategoryRequest
import com.ozatea.modules.category.presentation.CategoryResponse
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val repository: CategoryRepository,
    private val eventPublisher: CategoryEventPublisher
) {

    @Transactional
    fun create(request: CategoryRequest): CategoryResponse {
        val category = CategoryMapper.toEntity(request)
        val saved = repository.save(category)
        eventPublisher.publishCategoryCreatedEvent(CategoryCreatedEvent.from(saved))
        return CategoryMapper.toResponse(saved)
    }

    fun findAll(): List<CategoryResponse> {
        return repository.findAll().map { CategoryMapper.toResponse(it) }
    }

    fun findById(id: Int): CategoryResponse {
        val category = repository.findById(id) ?: throw EntityNotFoundException("Category not found: $id")
        return CategoryMapper.toResponse(category)
    }

    @Transactional
    fun deleteById(id: Int) {
        if (repository.findById(id) == null) {
            throw EntityNotFoundException("Category not found: $id")
        }
        repository.deleteById(id)
    }
}