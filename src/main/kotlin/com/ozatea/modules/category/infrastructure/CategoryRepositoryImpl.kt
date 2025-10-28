package com.ozatea.modules.category.infrastructure

import com.ozatea.modules.category.domain.Category
import com.ozatea.modules.category.domain.CategoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class CategoryRepositoryImpl(
    private val jpaRepository: JpaCategoryRepository
) : CategoryRepository {

    override fun findById(id: Int): Category? =
        jpaRepository.findByIdOrNull(id)

    override fun save(category: Category): Category =
        jpaRepository.save(category)

    override fun findAll(): List<Category> =
        jpaRepository.findAll()

    override fun deleteById(id: Int) {
        jpaRepository.deleteById(id)
    }
}
