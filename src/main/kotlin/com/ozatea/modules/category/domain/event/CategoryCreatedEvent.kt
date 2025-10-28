package com.ozatea.modules.category.domain.event

import com.ozatea.modules.category.domain.Category

data class CategoryCreatedEvent(
    val categoryId: Int,
    val name: String
) {
    companion object {
        fun from(category: Category) = CategoryCreatedEvent(
            categoryId = category.id,
            name = category.name
        )
    }
}