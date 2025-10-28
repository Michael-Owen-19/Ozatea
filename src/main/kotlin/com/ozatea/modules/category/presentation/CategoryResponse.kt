package com.ozatea.modules.category.presentation

data class CategoryResponse(
    val id: Int,
    val name: String,
    val description: String?,
    val parentId: Int?,
    val slug: String,
    val isActive: Boolean
)