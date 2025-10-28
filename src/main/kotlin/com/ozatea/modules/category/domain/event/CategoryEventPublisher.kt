package com.ozatea.modules.category.domain.event

fun interface CategoryEventPublisher {
    fun publishCategoryCreatedEvent(event: CategoryCreatedEvent)
}
