package com.ozatea.modules.category.domain

interface CategoryRepository {
    fun save(category: Category): Category
    fun findById(id: Int): Category?
    fun findAll(): List<Category>
    fun deleteById(id: Int)
}
