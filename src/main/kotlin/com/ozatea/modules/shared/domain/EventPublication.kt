package com.ozatea.modules.shared.domain

import com.ozatea.core.audit.AuditableEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

@Entity
@Table(name = "event_publication")
data class EventPublication(
    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID(),

    val listenerId: String,

    val eventType: String,

    val serializedEvent: String,

    val publicationDate: Instant? = null,

    val completionDate: Instant? = null
) : AuditableEntity()