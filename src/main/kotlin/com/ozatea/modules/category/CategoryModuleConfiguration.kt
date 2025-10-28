package com.ozatea.modules.category

import com.ozatea.modules.category.application.CategoryService
import com.ozatea.modules.category.domain.CategoryRepository
import com.ozatea.modules.category.domain.event.CategoryEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.modulith.ApplicationModule
import org.springframework.transaction.annotation.EnableTransactionManagement

@ApplicationModule(displayName = "Category Module")
@EnableTransactionManagement
@Configuration
class CategoryModuleConfiguration {
    @Bean
    fun categoryService(
        categoryRepository: CategoryRepository,
        categoryEventPusblisher: CategoryEventPublisher
    ): CategoryService {
        return CategoryService(categoryRepository, categoryEventPusblisher)
    }
}