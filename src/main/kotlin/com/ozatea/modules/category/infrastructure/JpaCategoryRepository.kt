package com.ozatea.modules.category.infrastructure

import com.ozatea.modules.category.domain.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaCategoryRepository : JpaRepository<Category, Int>
