package com.ozatea.modules.category.infrastructure

import com.ozatea.modules.category.domain.event.CategoryCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class CategoryEventListener {

    private val logger = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun onCategoryCreated(event: CategoryCreatedEvent) {
        logger.info("New category created: ${event.name} with ID=${event.categoryId}")
    }
}
