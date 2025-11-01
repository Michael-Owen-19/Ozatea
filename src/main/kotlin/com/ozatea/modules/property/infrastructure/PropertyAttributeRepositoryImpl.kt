package com.ozatea.modules.property.infrastructure

import com.ozatea.modules.property.domain.PropertyAttribute
import com.ozatea.modules.property.domain.PropertyAttributeRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class PropertyAttributeRepositoryImpl(
    private val jpaRepository: JpaPropertyAttributeRepository
) : PropertyAttributeRepository {
    override fun findById(id: Long): PropertyAttribute? =
        jpaRepository.findByIdOrNull(id)

    override fun save(propertyAttribute: PropertyAttribute): PropertyAttribute =
        jpaRepository.save(propertyAttribute)

    override fun saveAll(propertyAttributes: List<PropertyAttribute>): List<PropertyAttribute> =
        jpaRepository.saveAll(propertyAttributes)

    override fun deleteById(id: Long) =
        jpaRepository.deleteById(id)

    override fun findAll(): List<PropertyAttribute> =
        jpaRepository.findAll()

    override fun findByPropertyId(propertyId: Long): List<PropertyAttribute> =
        jpaRepository.findByPropertyId(propertyId)
}