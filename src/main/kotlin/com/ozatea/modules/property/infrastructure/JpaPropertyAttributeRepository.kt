package com.ozatea.modules.property.infrastructure

import com.ozatea.modules.property.domain.PropertyAttribute
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaPropertyAttributeRepository : JpaRepository<PropertyAttribute, Long> {
    fun findByPropertyId(propertyId: Long): List<PropertyAttribute>
}