package com.ozatea.modules.property.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PropertyRepository {
    fun save(property: Property): Property
    fun findById(id: Long): Property?
    fun findAll(pageable: Pageable): Page<Property>
    fun deleteById(id: Long)
}