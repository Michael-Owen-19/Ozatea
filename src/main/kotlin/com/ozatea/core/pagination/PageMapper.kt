package com.ozatea.core.pagination

import com.ozatea.core.response.PaginatedResponse
import org.springframework.data.domain.Page

fun <T, R> Page<T>.toPaginatedResponse(transform: (T) -> R): PaginatedResponse<R> {
    return PaginatedResponse(
        items = this.content.map(transform),
        totalItems = this.totalElements,
        totalPages = this.totalPages,
        currentPage = this.number,
        pageSize = this.size
    )
}