package com.ozatea.modules.property.presentation

import com.ozatea.core.response.ApiResponse
import com.ozatea.core.response.PaginatedResponse
import com.ozatea.modules.property.application.PropertyService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/properties")
class PropertyController(
    private val propertyService: PropertyService
    ) {
    @PostMapping
    fun create(@RequestBody request: PropertyRequest) : ResponseEntity<ApiResponse<PropertyResponse>>{
        val response = propertyService.create(request)
        return ResponseEntity.ok(ApiResponse.success(response, "Property created successfully"))
    }

    @GetMapping
    fun getALl(pageable: Pageable) : ResponseEntity<ApiResponse<PaginatedResponse<PropertyResponse>>>{
        val properties = propertyService.findAll(pageable)
        return ResponseEntity.ok(ApiResponse.success(properties, "Fetched all properties"))
    }
    
}