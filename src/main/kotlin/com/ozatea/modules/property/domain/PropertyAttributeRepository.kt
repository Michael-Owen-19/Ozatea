package com.ozatea.modules.property.domain

interface PropertyAttributeRepository {
    fun findById(id: Long): PropertyAttribute?
    fun save(propertyAttribute: PropertyAttribute): PropertyAttribute
    fun saveAll(propertyAttributes: List<PropertyAttribute>): List<PropertyAttribute>
    fun deleteById(id: Long)
    fun findAll(): List<PropertyAttribute>
    fun findByPropertyId(propertyId: Long): List<PropertyAttribute>
}