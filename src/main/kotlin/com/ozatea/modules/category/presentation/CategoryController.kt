package com.ozatea.modules.category.presentation

import com.ozatea.core.response.ApiResponse
import com.ozatea.modules.category.application.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/categories")
class CategoryController(
    private val categoryService: CategoryService
) {

    @PostMapping
    fun create(@RequestBody request: CategoryRequest): ResponseEntity<ApiResponse<CategoryResponse>> {
        val response = categoryService.create(request)
        return ResponseEntity.ok(ApiResponse.success(response, "Category created successfully"))
    }

    @GetMapping
    fun getAll(): ResponseEntity<ApiResponse<List<CategoryResponse>>> {
        val categories = categoryService.findAll()
        return ResponseEntity.ok(ApiResponse.success(categories, "Fetched all categories"))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<ApiResponse<CategoryResponse>> {
        val category = categoryService.findById(id)
        return ResponseEntity.ok(ApiResponse.success(category, "Category found"))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<ApiResponse<Nothing>> {
        categoryService.deleteById(id)
        return ResponseEntity.ok(ApiResponse.success(message = "Category deleted"))
    }
}