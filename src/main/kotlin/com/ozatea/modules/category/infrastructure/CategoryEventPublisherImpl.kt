package com.ozatea.modules.category.infrastructure

import com.ozatea.modules.category.domain.event.CategoryCreatedEvent
import com.ozatea.modules.category.domain.event.CategoryEventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class CategoryEventPublisherImpl(
    private val applicationEventPublisher: ApplicationEventPublisher
) : CategoryEventPublisher {

    override fun publishCategoryCreatedEvent(event: CategoryCreatedEvent) {
        applicationEventPublisher.publishEvent(event)
    }
}
