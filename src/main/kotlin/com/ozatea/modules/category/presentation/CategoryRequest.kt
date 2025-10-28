package com.ozatea.modules.category.presentation

data class CategoryRequest(
    val name: String,
    val description: String?,
    val parentId: Int?,
    val slug: String,
    val isActive: Boolean
)