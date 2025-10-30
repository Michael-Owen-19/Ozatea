package com.ozatea.modules.product.presentation

data class ProductRequest(
    val name: String,
    val description: String? = null,
    val slug: String,
    val categoryId: Int? = null
)