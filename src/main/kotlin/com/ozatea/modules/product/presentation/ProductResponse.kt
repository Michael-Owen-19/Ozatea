package com.ozatea.modules.product.presentation

import java.math.BigDecimal

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val basePrice: BigDecimal,
    val imageUrl: String?
)