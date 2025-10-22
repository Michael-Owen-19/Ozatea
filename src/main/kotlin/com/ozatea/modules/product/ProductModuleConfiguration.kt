package com.ozatea.modules.product

import com.ozatea.modules.product.application.ProductService
import com.ozatea.modules.product.domain.ProductRepository
import com.ozatea.modules.product.infrastructure.ProductEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.modulith.ApplicationModule
import org.springframework.transaction.annotation.EnableTransactionManagement

@ApplicationModule(displayName = "Product Module")
@EnableTransactionManagement
@Configuration
class ProductModuleConfiguration {

    /**
     * Provides ProductService bean for managing products and variants.
     */
    @Bean
    fun productService(
        productRepository: ProductRepository,
        productEventPublisher: ProductEventPublisher
    ): ProductService {
        return ProductService(productRepository, productEventPublisher)
    }
}