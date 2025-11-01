package com.ozatea.modules.property.application

import com.ozatea.core.pagination.toPaginatedResponse
import com.ozatea.core.response.PaginatedResponse
import com.ozatea.modules.property.domain.Property
import com.ozatea.modules.property.domain.PropertyAttribute
import com.ozatea.modules.property.domain.PropertyAttributeRepository
import com.ozatea.modules.property.domain.PropertyRepository
import com.ozatea.modules.property.presentation.PropertyAttributeResponse
import com.ozatea.modules.property.presentation.PropertyRequest
import com.ozatea.modules.property.presentation.PropertyResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PropertyService(
    private val propertyRepository: PropertyRepository,
    private val propertyAttributeRepository: PropertyAttributeRepository
) {
    @Transactional
    fun create(request: PropertyRequest): PropertyResponse {
        // 1️⃣ Save the main Property
        val property = Property(
            name = request.name,
            slug = request.slug
        )
        val savedProperty = propertyRepository.save(property)

        // 2️⃣ Prepare and save PropertyAttributes (if any)
        val propertyAttributes = request.attributes?.map {
            PropertyAttribute(
                property = savedProperty,
                value = it
            )
        } ?: emptyList()

        val savedPropertyAttributes = if (propertyAttributes.isNotEmpty()) {
            propertyAttributeRepository.saveAll(propertyAttributes)
        } else {
            emptyList()
        }

        // 3️⃣ Build the response
        return PropertyResponse(
            id = savedProperty.id,
            name = savedProperty.name,
            slug = savedProperty.slug,
            attributeList = savedPropertyAttributes.map {
                PropertyAttributeResponse(
                    id = it.id,
                    value = it.value
                )
            }
        )
    }

    fun findAll(pageable: Pageable): PaginatedResponse<PropertyResponse> {
        val properties = propertyRepository.findAll(pageable)

        return properties.toPaginatedResponse { property ->
            val attributes = propertyAttributeRepository.findByPropertyId(property.id).map {
                PropertyAttributeResponse(
                    id = it.id,
                    value = it.value
                )
            }

            PropertyResponse(
                id = property.id,
                name = property.name,
                slug = property.slug,
                attributeList = attributes
            )
        }
    }


}
