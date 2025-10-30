package com.ozatea.modules.media.domain

import com.ozatea.core.audit.AuditableEntity
import com.ozatea.core.enums.MediaType
import com.ozatea.core.enums.StorageType
import jakarta.persistence.*

@Entity
@Table(name = "media")
data class Media(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val filename: String,

    @Column(name = "original_filename")
    val originalFilename: String,

    @Column(name = "alt_text")
    val altText: String?,

    val filepath: String,

    @Column(name = "mime_type")
    val mimeType: String,

    @Column(name = "media_type")
    @Enumerated(EnumType.STRING)
    val mediaType: MediaType,

    val size: Long,

    @Column(name = "storage_type")
    @Enumerated(EnumType.STRING)
    val storageType: StorageType
) : AuditableEntity()