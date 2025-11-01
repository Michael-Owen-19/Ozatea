package com.ozatea.modules.property.presentation

data class PropertyRequest (
    val name: String,
    val slug: String,
    val attributes: List<String>?
)