package com.ozatea.modules.product.domain

import com.ozatea.modules.product.domain.event.ProductCreatedEvent

interface ProductEventPublisher {
    fun publishProductCreatedEvent(event: ProductCreatedEvent)
}
