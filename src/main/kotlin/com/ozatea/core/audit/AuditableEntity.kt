package com.ozatea.core.audit

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.Filter
import org.hibernate.annotations.FilterDef
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@FilterDef(name = "notDeleted", autoEnabled = true)
@Filter(name = "notDeleted", condition = "deleted_at IS NULL")
abstract class AuditableEntity {
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    var createdAt: Instant? = null

    @CreatedBy
    @Column(name = "created_by")
    var createdBy: UUID? = null

    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: Instant? = null

    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: UUID? = null

    @Column(name = "deleted_at")
    var deletedAt: Instant? = null

    @Column(name = "deleted_by")
    var deletedBy: UUID? = null
}