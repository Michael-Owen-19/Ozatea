package com.ozatea.modules.product.infrastructure

import com.ozatea.modules.product.domain.event.ProductCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ProductEventListener {

    private val logger = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun onProductCreated(event: ProductCreatedEvent) {
        logger.info("New product created: ${event.name} with ID=${event.productId}")
    }
}
