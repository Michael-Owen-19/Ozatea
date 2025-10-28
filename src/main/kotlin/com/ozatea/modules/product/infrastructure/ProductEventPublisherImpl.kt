package com.ozatea.modules.product.infrastructure

import com.ozatea.modules.product.domain.event.ProductCreatedEvent
import com.ozatea.modules.product.domain.event.ProductEventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ProductEventPublisherImpl(
    private val applicationEventPublisher: ApplicationEventPublisher
) : ProductEventPublisher {

    override fun publishProductCreatedEvent(event: ProductCreatedEvent) {
        applicationEventPublisher.publishEvent(event)
    }
}
