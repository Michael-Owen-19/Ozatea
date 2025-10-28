package com.ozatea.modules.category.infrastructure

import com.ozatea.modules.category.domain.Category
import com.ozatea.modules.category.presentation.CategoryRequest
import com.ozatea.modules.category.presentation.CategoryResponse

object CategoryMapper {
    fun toEntity(request: CategoryRequest): Category{
        return Category(
            name = request.name,
            description = request.description,
            slug = request.slug,
            isActive = request.isActive
        )
    }

    fun toResponse(category: Category): CategoryResponse {
        return CategoryResponse(
            id = category.id,
            name = category.name,
            description = category.description,
            slug = category.slug,
            parentId = category.parent?.id,
            isActive = category.isActive
        )
    }
}