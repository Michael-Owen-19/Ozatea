package com.ozatea.modules.product.presentation

import com.ozatea.core.response.ApiResponse
import com.ozatea.core.response.PaginatedResponse
import com.ozatea.modules.product.application.ProductService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
) {

    @PostMapping
    fun create(@RequestBody request: ProductRequest): ResponseEntity<ApiResponse<ProductResponse>> {
        val response = productService.create(request)
        return ResponseEntity.ok(ApiResponse.success(response, "Product created successfully"))
    }

    @GetMapping
    fun getAll(pageable: Pageable): ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>> {
        val products = productService.findAll(pageable)
        return ResponseEntity.ok(ApiResponse.success(products, "Fetched all products"))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<ApiResponse<ProductResponse>> {
        val product = productService.findById(id)
        return ResponseEntity.ok(ApiResponse.success(product, "Product found"))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<ApiResponse<Nothing>> {
        productService.deleteById(id)
        return ResponseEntity.ok(ApiResponse.success(message = "Product deleted"))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: ProductRequest): ResponseEntity<ApiResponse<ProductResponse>> {
        val updatedProduct = productService.update(id, request)
        return ResponseEntity.ok(ApiResponse.success(updatedProduct, "Product updated successfully"))
    }
}