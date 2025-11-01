package com.ozatea.modules.property.presentation

data class PropertyResponse (
    val id: Long,
    val name: String,
    val slug: String,
    val attributeList: List<PropertyAttributeResponse>
)

data class PropertyAttributeResponse (
    val id: Long,
    val value: String
)