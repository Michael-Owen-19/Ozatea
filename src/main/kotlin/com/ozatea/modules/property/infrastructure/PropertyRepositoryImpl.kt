package com.ozatea.modules.property.infrastructure

import com.ozatea.modules.property.domain.Property
import com.ozatea.modules.property.domain.PropertyRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class PropertyRepositoryImpl(
    private val jpaRepository: JpaPropertyRepository
) : PropertyRepository {
    override fun save(property: Property): Property =
        jpaRepository.save(property)


    override fun findById(id: Long): Property? =
        jpaRepository.findByIdOrNull(id)

    override fun findAll(pageable: Pageable): Page<Property> =
        jpaRepository.findAll(pageable)

    override fun deleteById(id: Long) =
        jpaRepository.deleteById(id)

}