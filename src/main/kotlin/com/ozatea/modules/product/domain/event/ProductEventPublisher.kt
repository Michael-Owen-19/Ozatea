package com.ozatea.modules.product.domain.event

fun interface ProductEventPublisher {
    fun publishProductCreatedEvent(event: ProductCreatedEvent)
}
